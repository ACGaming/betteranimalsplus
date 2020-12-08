package its_meow.betteranimalsplus.common.entity;

import javax.annotation.Nullable;

import com.google.common.base.Predicates;

import dev.itsmeow.imdlib.entity.util.IVariant;
import dev.itsmeow.imdlib.entity.util.IVariantTypes;
import its_meow.betteranimalsplus.common.entity.ai.EntityAIEatBerries;
import its_meow.betteranimalsplus.common.entity.util.EntityTypeContainerBAP;
import its_meow.betteranimalsplus.init.ModEntities;
import its_meow.betteranimalsplus.init.ModLootTables;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class EntityBearNeutral extends EntityBear implements IVariantTypes<EntityBear> {

    public EntityBearNeutral(World worldIn) {
        super(ModEntities.BLACK_BEAR.entityType, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new EntityBearNeutral.AIMeleeAttack());
        this.goalSelector.addGoal(2, new EntityAIEatBerries(this, 1.0D, 12, 2));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.5D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<ChickenEntity>(this, ChickenEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<RabbitEntity>(this, RabbitEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<EntityPheasant>(this, EntityPheasant.class, 90,
        true, true, Predicates.alwaysTrue()));
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, CompoundNBT compound) {
        return this.initData(world, reason, super.onInitialSpawn(world, difficulty, reason, livingdata, compound));
    }

    @Override
    public IVariant getRandomType() {
        return this.getContainer().getVariantForName(this.getRNG().nextFloat() <= 0.125F ? "kermode" : "black");
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.registerTypeKey();
    }

    @Override
    public boolean writeUnlessRemoved(CompoundNBT compound) {
        this.writeType(compound);
        return super.writeUnlessRemoved(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.readType(compound);
    }

    @Override
    protected ResourceLocation getLootTable() {
        switch(this.getVariantNameOrEmpty()) {
        case "black":
            return ModLootTables.BEAR_BLACK;
        case "kermode":
            return ModLootTables.BEAR_KERMODE;
        default:
            return ModLootTables.BEAR_BLACK;
        }
    }

    @Override
    public EntityTypeContainerBAP<EntityBearNeutral> getContainer() {
        return ModEntities.BLACK_BEAR;
    }

    @Override
    public EntityBearNeutral getImplementation() {
        return this;
    }

    @Override
    public boolean canDespawn(double range) {
        return despawn(range);
    }

}
