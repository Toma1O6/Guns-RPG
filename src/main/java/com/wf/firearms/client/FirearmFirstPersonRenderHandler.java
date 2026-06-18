package com.wf.firearms.client;

/**
 * 已停用：第一人称改由 {@link com.wf.firearms.client.render.GunsRpgItemRenderer}（BEWLR）绘制。
 * 此前 {@code RenderHandEvent} + {@code setCanceled} 与 BEWLR 变换栈不一致，仅 AK 等少数枪可见。
 */
public final class FirearmFirstPersonRenderHandler {
    private FirearmFirstPersonRenderHandler() {}
}
