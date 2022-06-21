package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.*;
import dev.toma.gunsrpg.client.screen.skill.ExtensionsView;
import dev.toma.gunsrpg.client.screen.skill.IViewManager;
import dev.toma.gunsrpg.client.screen.skill.View;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_SkillClickedPacket;
import dev.toma.gunsrpg.network.packet.C2S_UnlockSkillPacket;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class SkillInfoWidget extends ContainerWidget {

    private static final ITextComponent EXTENSIONS = new TranslationTextComponent("view.skill.extensions");
    private static final ITextComponent PURCHASE = new TranslationTextComponent("view.skill.purchase");
    private static final ITextComponent USE = new TranslationTextComponent("view.skill.use");
    private static final ITextComponent UNKNOWN = new TranslationTextComponent("view.skill.unknown");
    private static final ITextComponent NO_CONTENT = new TranslationTextComponent("screen.text.no_content");

    private final IViewManager manager;
    private SkillType<?> src;
    private ITextComponent title;
    private ITextComponent[] description;
    private boolean extensions;
    private boolean clickable;

    private Button buyWidget;
    private Button useWidget;

    public SkillInfoWidget(int x, int y, int width, int height, IViewManager manager) {
        super(x, y, width, height);
        this.manager = manager;
        init();
    }

    public void updateSource(SkillType<?> type) {
        this.src = type;
        this.clickable = false;
        if (src != null) {
            title = type.getTitle();
            description = type.getDescription();
            extensions = !ModUtils.isNullOrEmpty(type.getHierarchy().getExtensions());
            ISkillProvider provider = manager.getContext().getData().getSkillProvider();
            ISkill top = SkillUtil.getTopHierarchySkill(src, provider);
            clickable = src.getDataInstance() instanceof IClickableSkill && (top == null || top.getType().equals(src));
        }
        init();
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        Matrix4f pose = stack.last().pose();
        RenderUtils.drawSolid(pose, x, y, x + width, y + height, 0xAB << 24);

        super.renderButton(stack, mouseX, mouseY, partialTicks);
    }

    private void init() {
        clear();
        if (src == null) {
            addWidget(new Label(x, y, width, height, NO_CONTENT));
            return;
        }
        ISkillProperties properties = src.getProperties();
        boolean invisible = properties.getRequiredLevel() > manager.getContext().getData().getProgressData().getLevel();
        int unlockState = this.getUnlockState();
        addWidget(new Icon(x + 5, y + 5, 16, 16, invisible ? SkillWidget.UNKNOWN : src.getDisplayData()));
        addWidget(new Label(x + 26, y + 5, width - 100, 15, invisible ? StringTextComponent.EMPTY : title));
        int rightColX = x + width - 90;
        Button ext = addWidget(new Button(rightColX, y + 5, 80, 20, EXTENSIONS, this::showExtensionsView));
        ext.active = extensions;
        ext.visible = !invisible;
        buyWidget = addWidget(new Button(rightColX, y + 27, 80, 20, PURCHASE, this::purchaseClicked));
        buyWidget.active = !invisible && unlockState == 1;
        buyWidget.visible = !invisible && unlockState > 0;
        if (!invisible && clickable) {
            useWidget = addWidget(new Button(rightColX, y + 27, 80, 20, USE, this::useClicked));
            useWidget.visible = unlockState == 0;
            useWidget.active = false;
        }

        ITextComponent levelCondiditon = invisible ? StringTextComponent.EMPTY : new TranslationTextComponent("view.skill.required_level", properties.getRequiredLevel());
        ITextComponent price = invisible ? StringTextComponent.EMPTY : new TranslationTextComponent("view.skill.price", properties.getPrice());
        addWidget(new Label(rightColX, y + 50, 80, 15, levelCondiditon));
        addWidget(new Label(rightColX, y + 65, 80, 15, price));

        FontRenderer renderer = Minecraft.getInstance().font;
        if (invisible) {
            addWidget(new Label(x + 5, y + 25, 100, 10, UNKNOWN));
            return;
        }
        int maxWidth = width - 130;
        int lineIndex = 0;
        for (ITextComponent component : description) {
            List<IReorderingProcessor> list = renderer.split(component, maxWidth);
            for (IReorderingProcessor processor : list) {
                addWidget(new Label(x + 5, y + 25 + (lineIndex++ * 10), maxWidth, 10, processor));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (clickable && useWidget != null) {
            ISkillProvider provider = manager.getContext().getData().getSkillProvider();
            ISkill skill = SkillUtil.getTopHierarchySkill(src, provider);
            if (skill != null) {
                useWidget.active = ((IClickableSkill) skill).canUse();
            } else useWidget.active = false;
        }
    }

    private void showExtensionsView(Button button) {
        int width = manager.getWidth();
        int height = manager.getHeight();
        View last = manager.getView();
        manager.setView(new ExtensionsView(width, height, manager, last, src));
    }

    private void useClicked(Button button) {
        NetworkManager.sendServerPacket(new C2S_SkillClickedPacket(validateSourceClickable()));
        useWidget.active = false;
    }

    private void purchaseClicked(Button button) {
        NetworkManager.sendServerPacket(new C2S_UnlockSkillPacket(src));
        buyWidget.visible = false;
        if (clickable) {
            useWidget.visible = true;
        }
    }

    private int getUnlockState() {
        IPlayerData data = manager.getContext().getData();
        ISkillProperties properties = src.getProperties();
        ISkillHierarchy<?> hierarchy = src.getHierarchy();
        ITransactionValidator validator = properties.getTransactionValidator();
        ISkillProvider provider = data.getSkillProvider();
        SkillType<?> parentType = hierarchy.getParent();
        if (provider.hasSkill(src)) {
            return 0;
        } else {
            return (provider.hasSkill(parentType) || parentType == null) && validator.canUnlock(data, src) ? 1 : 2;
        }
    }

    @SuppressWarnings("unchecked")
    private <S extends ISkill & IClickableSkill, T extends SkillType<S>> T validateSourceClickable() {
        ISkill skill = src.getDataInstance();
        if (skill instanceof IClickableSkill) {
            return (T) src;
        }
        throw new IllegalArgumentException("Attempted to send C2S click event with unlickable skill type [" + src + "]");
    }

    private static class Label extends Widget {

        private IReorderingProcessor drawable;

        Label(int x, int y, int width, int height, ITextComponent label) {
            super(x, y, width, height, label);
        }

        Label(int x, int y, int width, int height, IReorderingProcessor processor) {
            this(x, y, width, height, StringTextComponent.EMPTY);
            this.drawable = processor;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            FontRenderer renderer = Minecraft.getInstance().font;
            float yCenter = RenderUtils.getVerticalCenter(renderer, this);
            if (drawable == null) {
                renderer.drawShadow(matrix, getMessage(), x, yCenter, 0xFFFFFF);
            } else {
                renderer.drawShadow(matrix, drawable, x, yCenter, 0xFFFFFF);
            }
        }
    }

    private static class Icon extends Widget {

        final DisplayData data;

        Icon(int x, int y, int width, int height, DisplayData data) {
            super(x, y, width, height, StringTextComponent.EMPTY);
            this.data = data;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            data.renderAt(matrix, x, y);
        }
    }
}
