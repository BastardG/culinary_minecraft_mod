package ru.bastard.culinary.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import ru.bastard.culinary.block.CuttingBoard;
import ru.bastard.culinary.block.entity.CuttingBoardEntity;

public class CuttingBoardEntityRenderer implements BlockEntityRenderer<CuttingBoardEntity> {

    public CuttingBoardEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CuttingBoardEntity entity, float partialTick, PoseStack pose,
                       MultiBufferSource source, int packedLight, int packedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = entity.getRenderStack();
        Direction direction = entity.getBlockState().getValue(CuttingBoard.FACING).getOpposite();

        pose.pushPose();
        pose.translate(0.5F, 0.1F, 0.5F);
        pose.scale(0.5F, 0.5F, 0.5F);
        pose.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        pose.mulPose(Axis.XP.rotationDegrees(90));

        switch (entity.getBlockState().getValue(CuttingBoard.FACING)) {
            case NORTH -> pose.mulPose(Axis.ZP.rotationDegrees(0));
            case EAST -> pose.mulPose(Axis.ZP.rotationDegrees(90));
            case SOUTH -> pose.mulPose(Axis.ZP.rotationDegrees(180));
            case WEST -> pose.mulPose(Axis.ZP.rotationDegrees(270));
        }

        itemRenderer.renderStatic(
                itemStack, ItemTransforms.TransformType.GUI,
                getLightLevel(entity.getLevel(), entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pose, source, 1);
        pose.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

}
