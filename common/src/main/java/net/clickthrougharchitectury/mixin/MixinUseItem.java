package net.clickthrougharchitectury.mixin;

import net.clickthrougharchitectury.ClickThroughArchitectury;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinUseItem {

    @Shadow public HitResult hitResult;
    @Shadow public Entity crosshairPickEntity;
    @Shadow public ClientLevel level;
    @Shadow public LocalPlayer player;

    public boolean crouchNext = false;
    /*
    https://jenkins.liteloader.com/job/Mixin/javadoc/index.html?org/spongepowered/asm/mixin/injection/struct/MemberInfo.html

    ItemStack itemStack = this.player.getItemInHand(interactionHand);
        return type (descriptor):   net.minecraft.world.item.ItemStack
        instance type (owner) :     net.minecraft.client.player.LocalPlayer
        method name:                getItemInHand
        method arg type:            net.minecraft.world.InteractionHand
    Final Explcit Target selector:
        net.minecraft.client.player.LocalPlayer;getItemInHand(net.minecraft.world.InteractionHand)net.minecraft.world.item.ItemStack
        Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand);Lnet/minecraft/world/item/ItemStack
    */

    @Inject(
            method = "startUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
                    //doesn't work
                    //target = "net.minecraft.client.player.LocalPlayer.getItemInHand(net.minecraft.world.InteractionHand)net.minecraft.world.item.ItemStack"
            )
    )
    public void useThrough(CallbackInfo ci) {
        if (hitResult != null) {
            //System.out.println(hitResult.getType());
            switch (hitResult.getType()) {
                case ENTITY -> { // item frames
                    if (
                            ClickThroughArchitectury.item_frame_enabled &&
                            crosshairPickEntity instanceof ItemFrame &&
                            !player.isCrouching()
                    ) {
                        //System.out.println("Item Frame");
                        this.hitResult = useThroughItemFrame((ItemFrame) crosshairPickEntity, hitResult);
                    }
                }
                case BLOCK -> { // signs & banners
                    BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                    BlockState state = level.getBlockState(blockPos);
                    Block block = state.getBlock();
                    System.out.println(block);

                    if (
                            ClickThroughArchitectury.sign_enabled &&
                            block instanceof WallSignBlock
                    ) {
                        //System.out.println("Sign");
                        Item heldItem = player.getMainHandItem().getItem();

                        if (!player.isCrouching()) {
                            this.hitResult = useThroughSign(blockPos, state, hitResult);
                        } else if (heldItem instanceof DyeItem || heldItem == Items.GLOW_INK_SAC) { // allow dying when shift is held
                            //System.out.println("Dye");
                            player.setShiftKeyDown(false);
                            player.connection.send(
                                    new ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY, 0)
                            );
                        }
                    }
                    if (
                            ClickThroughArchitectury.banner_enabled &&
                            block instanceof WallBannerBlock
                    ) {
                        System.out.println("Banner");
                        this.hitResult = useThroughBanner(blockPos, state, hitResult);
                    }
                }
            }
        }
    }

    private boolean attached_valid(BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        BlockEntity entity = level.getBlockEntity(pos);

        return (!ClickThroughArchitectury.only_containers || entity instanceof BaseContainerBlockEntity) &&
                (ClickThroughArchitectury.to_sign || !(block instanceof WallSignBlock));
    }

    private HitResult useThroughItemFrame(ItemFrame frame, HitResult hitResult) {
        BlockPos attachedPos = frame.getPos().offset(frame.getDirection().getOpposite().getNormal());

        if (attached_valid(attachedPos)) {
            return new BlockHitResult(hitResult.getLocation(), frame.getDirection(), attachedPos, false);
        } else {
            return null;
        }
    }

    private HitResult useThroughSign(BlockPos blockPos, BlockState state, HitResult hitResult) {
        Direction dir = (Direction) state.getValues().get(WallSignBlock.FACING);
        BlockPos attachedPos = blockPos.offset(dir.getOpposite().getNormal());
        if (attached_valid(attachedPos)) {
            return new BlockHitResult(hitResult.getLocation(), dir, attachedPos, false);
        } else {
            return null;
        }
    }

    private HitResult useThroughBanner(BlockPos blockPos, BlockState state, HitResult hitResult) {
        Direction dir = (Direction) state.getValues().get(WallBannerBlock.FACING);
        BlockPos attachedPos = blockPos.offset(dir.getOpposite().getNormal());
        if (attached_valid(attachedPos)) {
            return new BlockHitResult(hitResult.getLocation(), dir, attachedPos, false);
        } else {
            return null;
        }
    }

    @Inject(method = "startUseItem", at = @At("RETURN"))
    public void reCrouch(CallbackInfo ci){
        if (crouchNext) {
            player.setShiftKeyDown(true);
            player.connection.send(
                    new ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY, 0)
            );
        }
    }
}
