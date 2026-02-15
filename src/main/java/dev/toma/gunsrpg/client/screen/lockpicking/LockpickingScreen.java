package dev.toma.gunsrpg.client.screen.lockpicking;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_TestPinCombination;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class LockpickingScreen extends Screen {

    public static final ITextComponent TITLE = new TranslationTextComponent("screen.gunsrpg.lockpicking").withStyle(TextFormatting.UNDERLINE);

    private final BlockPos position;
    private final int pinCount;
    private final IntList currentPins = new IntArrayList();

    public LockpickingScreen(BlockPos position, int pinCount) {
        super(TITLE);
        this.position = position;
        this.pinCount = pinCount;
    }

    @Override
    protected void init() {
        int pinWidgetScale = 20;
        int pinsWidth = this.pinCount * pinWidgetScale;
        int startX = (this.width - pinsWidth) / 2;
        for (int i = 0; i < this.pinCount; i++) {
            PinWidget widget = this.addButton(new PinWidget(startX + i * pinWidgetScale, 50, pinWidgetScale, pinWidgetScale, i, this::onPinPressed));
            widget.active = !this.currentPins.contains(i);
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float renderDelta) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, renderDelta);

        this.font.drawShadow(matrix, TITLE, (this.width - this.font.width(TITLE)) / 2.0F, 20, 0xFFFFFF);
    }

    private void onPinPressed(int pinIndex) {
        this.currentPins.add(pinIndex);
        NetworkManager.sendServerPacket(new C2S_TestPinCombination(this.position, this.currentPins));
        this.init(this.minecraft, this.width, this.height);
    }
}
