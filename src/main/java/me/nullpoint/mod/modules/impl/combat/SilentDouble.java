package me.nullpoint.mod.modules.impl.combat;



import me.nullpoint.api.managers.CommandManager;
import me.nullpoint.api.utils.entity.EntityUtil;
import me.nullpoint.mod.modules.Module;
import me.nullpoint.mod.modules.impl.player.PacketMine;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

public class SilentDouble extends Module {

    public static int slotMain;
    public static int swithc2;

    public static SilentDouble INSTANCE;
    public SilentDouble(){
        super("SilentDouble", Category.Combat);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        update();
    }

    public void update() {
        if (!PacketMine.INSTANCE.isOn()) {
            CommandManager.sendChatMessage("\u00a7e[?] \u00a7c\u00a7oAutoMine?");
            this.disable();
            return;
        }
        if (PacketMine.secondPos != null && !PacketMine.INSTANCE.secondTimer.passed(PacketMine.INSTANCE.getBreakTime(PacketMine.secondPos, PacketMine.INSTANCE.getTool(PacketMine.secondPos) == -1 ? mc.player.getInventory().selectedSlot : PacketMine.INSTANCE.getTool(PacketMine.secondPos), 0.89))) {
            slotMain = mc.player.getInventory().selectedSlot;
        }
        if (PacketMine.secondPos != null) {
            if (PacketMine.INSTANCE.secondTimer.passed(PacketMine.INSTANCE.getBreakTime(PacketMine.secondPos, PacketMine.INSTANCE.getTool(PacketMine.secondPos), 0.90))) {
                if (mc.player.getMainHandStack().getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                    if (!mc.options.useKey.isPressed()) {
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(PacketMine.INSTANCE.getTool(PacketMine.secondPos)));
                        swithc2 = 1;
                    } else {
                        if (swithc2 == 1){
                            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slotMain));
                            EntityUtil.syncInventory();
                        }
                    }
                } else {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(PacketMine.INSTANCE.getTool(PacketMine.secondPos)));
                    swithc2 = 1;
                }
            }
        }
        if (PacketMine.secondPos != null && PacketMine.INSTANCE.secondTimer.passed(PacketMine.INSTANCE.getBreakTime(PacketMine.secondPos, PacketMine.INSTANCE.getTool(PacketMine.secondPos), 1.2))) {
            if (swithc2 == 1) {
                mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slotMain));
                EntityUtil.syncInventory();
            }
        }
    }
}
