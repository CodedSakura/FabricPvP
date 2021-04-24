package eu.codedsakura.fabricpvp;


import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class PlayerComponentInitializer implements EntityComponentInitializer {
    public static final ComponentKey<IPvPComponent> PVP_DATA =
            ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("fabricpvp", "pvp"), IPvPComponent.class);


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PVP_DATA, playerEntity -> new PvPComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    public interface IPvPComponent extends ComponentV3 {
        boolean isOn();
        void set(boolean value);
    }

    public static class PvPComponent implements IPvPComponent {
        private boolean isOn = false;

        @Override
        public boolean isOn() {
            return isOn;
        }

        @Override
        public void set(boolean value) {
            isOn = value;
        }

        @Override
        public void readFromNbt(CompoundTag tag) {
            isOn = tag.getBoolean("on");
        }

        @Override
        public void writeToNbt(CompoundTag tag) {
            tag.putBoolean("on", isOn);
        }
    }
}
