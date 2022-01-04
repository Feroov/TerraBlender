/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * Creative Commons Attribution-NonCommercial-NoDerivatives 4.0.
 ******************************************************************************/
package terrablender.mixin.client;

import net.minecraft.client.gui.screens.worldselection.WorldPreset;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import terrablender.api.GenerationSettings;

import java.util.Optional;

@Mixin(targets = "net.minecraft.client.gui.screens.worldselection.WorldPreset$1")
public abstract class MixinWorldPreset extends WorldPreset
{
    public MixinWorldPreset(Component displayName)
    {
        super(displayName);
    }

    @Shadow
    abstract protected ChunkGenerator generator(RegistryAccess registryAccess, long seed);

    @Inject(method = "generator", at = @At("HEAD"), cancellable = true)
    public void modifyGenerator(RegistryAccess registryAccess, long seed, CallbackInfoReturnable<ChunkGenerator> cir)
    {
        Optional<ChunkGenerator> generator = GenerationSettings.getDefaultChunkGeneratorOverride();

        if (generator.isPresent())
        {
            cir.setReturnValue(generator.get());
        }
    }

    @Override
    public WorldGenSettings create(RegistryAccess.RegistryHolder registryAccess, long seed, boolean generateFeatures, boolean generateBonusChest)
    {
        Optional<WorldGenSettings> settings = GenerationSettings.getDefaultWorldGenSettingsOverride();

        if (settings.isPresent()) return settings.get();
        else return super.create(registryAccess, seed, generateFeatures, generateBonusChest);
    }
}
