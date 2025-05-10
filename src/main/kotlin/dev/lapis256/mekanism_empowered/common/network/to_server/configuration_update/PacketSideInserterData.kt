package dev.lapis256.mekanism_empowered.common.network.to_server.configuration_update

import dev.lapis256.mekanism_empowered.extension.inserterConfig
import mekanism.api.RelativeSide
import mekanism.common.network.IMekanismPacket
import mekanism.common.tile.prefab.TileEntityConfigurableMachine
import mekanism.common.util.WorldUtils
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent


data class PacketSideInserterData(private val pos: BlockPos, private val side: RelativeSide) : IMekanismPacket {
    override fun handle(context: NetworkEvent.Context) {
        val blockEntity = WorldUtils.getTileEntity(context.sender?.level(), pos) ?: return
        val tile = blockEntity as? TileEntityConfigurableMachine ?: return
        tile.inserterConfig.toggleSideConfig(side)
    }

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeBlockPos(pos)
        buffer.writeEnum(side)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf) =
            PacketSideInserterData(buffer.readBlockPos(), buffer.readEnum(RelativeSide::class.java))
    }
}
