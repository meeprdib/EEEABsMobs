package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.world.structure.AncientTombStructure;
import com.eeeab.eeeabsmobs.sever.world.structure.BloodyAltarStructure;
import com.eeeab.eeeabsmobs.sever.world.structure.EMConfigurationStructure;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructuresInit {
    private static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, EEEABMobs.MOD_ID);
    private static final DeferredRegister<StructurePieceType> PIECE_TYPE = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, EEEABMobs.MOD_ID);
    public static final RegistryObject<StructureType<EMConfigurationStructure>> CONFIG_STRUCTURES = STRUCTURES.register("em_config", () -> explicitStructureTypeTyping(EMConfigurationStructure.CODEC));
    public static final RegistryObject<StructureType<AncientTombStructure>> ANCIENT_TOMB_STRUCTURE = STRUCTURES.register("ancient_tomb", () -> explicitStructureTypeTyping(AncientTombStructure.CODEC));
    public static final RegistryObject<StructureType<BloodyAltarStructure>> BLOODY_ALTAR_STRUCTURE = STRUCTURES.register("bloody_altar", () -> explicitStructureTypeTyping(BloodyAltarStructure.CODEC));
    public static final RegistryObject<StructurePieceType> ATP = PIECE_TYPE.register("ancient_tomb", () -> AncientTombStructure.Piece::new);
    public static final RegistryObject<StructurePieceType> BA = PIECE_TYPE.register("bloody_altar", () -> BloodyAltarStructure.Piece::new);

    /**
     * Originally, I had a double lambda ()->()-> for the RegistryObject line above, but it turns out that
     * some IDEs cannot resolve the typing correctly. This method explicitly states what the return type
     * is so that the IDE can put it into the DeferredRegistry properly.
     */
    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }

    public static void register(IEventBus iEventBus) {
        STRUCTURES.register(iEventBus);
        PIECE_TYPE.register(iEventBus);
    }
}
