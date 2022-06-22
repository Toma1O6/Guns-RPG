// Initializes coremod
function initializeCoreMod() {
    Opcodes = Java.type('org.objectweb.asm.Opcodes');
    ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
    FieldNode = Java.type('org.objectweb.asm.tree.FieldNode');
    MethodNode = Java.type('org.objectweb.asm.tree.MethodNode');
    AbstractInsnNode = Java.type('org.objectweb.asm.tree.AbstractInsnNode');
    InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');

    ACC_PUBLIC = Opcodes.ACC_PUBLIC;
    INVOKESTATIC = Opcodes.INVOKESTATIC;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
    INVOKESPECIAL = Opcodes.INVOKESPECIAL;
    ALOAD = Opcodes.ALOAD;
    ILOAD = Opcodes.ILOAD;
    FLOAD = Opcodes.FLOAD;
    DLOAD = Opcodes.DLOAD;
    ISTORE = Opcodes.ISTORE;
    RETURN = Opcodes.RETURN;
    ARETURN = Opcodes.ARETURN;
    IRETURN = Opcodes.IRETURN;
    DRETURN = Opcodes.DRETURN;
    NEW = Opcodes.NEW;
    ACONST_NULL = Opcodes.ACONST_NULL;
    ICONST_0 = Opcodes.ICONST_0;
    IFEQ = Opcodes.IFEQ;
    IFNE = Opcodes.IFNE;
    IF_ACMPEQ = Opcodes.IF_ACMPEQ;
    GETFIELD = Opcodes.GETFIELD;
    GETSTATIC = Opcodes.GETSTATIC;
    GOTO = Opcodes.GOTO;
    LABEL = AbstractInsnNode.LABEL;
    METHOD_INSN = AbstractInsnNode.METHOD_INSN;

    return {
        attackDelayPatch: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.entity.player.PlayerEntity',
                methodName: 'func_184818_cX',
                methodDesc: '()F'
            },
            transformer: function (methodNode) {
                patchAttackDelay(methodNode.instructions);
                return methodNode;
            }
        },

        followDistancePatch: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.entity.ai.goal.TargetGoal',
                methodName: 'func_111175_f',
                methodDesc: '()D'
            },
            transformer: function (methodNode) {
                patchFollowDistance(methodNode.instructions);
                return methodNode;
            }
        },

        blockDropsPatch: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.block.AbstractBlock',
                methodName: 'func_220076_a',
                methodDesc: '(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/LootContext$Builder;)Ljava/util/List;'
            },
            transformer: function (methodNode) {
                patchBlockDrops(methodNode.instructions);
                return methodNode;
            }
        },

        enderDragonConstructorPatch: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.entity.boss.dragon.EnderDragonEntity',
                methodName: '<init>',
                methodDesc: '(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V'
            },
            transformer: function (methodNode) {
                patchEnderDragonConstructor(methodNode.instructions);
                return methodNode;
            }
        },

        freshEntitySpawnHookPatch: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.server.ServerWorld',
                methodName: 'func_217376_c',
                methodDesc: '(Lnet/minecraft/entity/Entity;)Z'
            },
            transformer: function (methodNode) {
                addFreshEntitySpawnHook(methodNode.instructions);
                return methodNode;
            }
        },

        ambientParticle: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.biome.Biome',
                methodName: 'func_235090_t_',
                methodDesc: '()Ljava/util/Optional;'
            },
            transformer: function (methodNode) {
                patchAmbientParticle(methodNode.instructions);
                return methodNode;
            }
        },

        ambientSoundLoop: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.biome.Biome',
                methodName: 'func_235091_u_',
                methodDesc: '()Ljava/util/Optional;'
            },
            transformer: function (methodNode) {
                patchAmbientLoopSound(methodNode.instructions);
                return methodNode;
            }
        },

        ambientMood: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.biome.Biome',
                methodName: 'func_235092_v_',
                methodDesc: '()Ljava/util/Optional;'
            },
            transformer: function (methodNode) {
                patchAmbientMood(methodNode.instructions);
                return methodNode;
            }
        }
    }
}

/*
public getCurrentItemAttackStrengthDelay()F
 L0
  LINENUMBER 1907 L0
  DCONST_1
  ALOAD 0
  GETSTATIC net/minecraft/entity/ai/attributes/Attributes.ATTACK_SPEED : Lnet/minecraft/entity/ai/attributes/Attribute;        // remove
  INVOKEVIRTUAL net/minecraft/entity/player/PlayerEntity.getAttributeValue (Lnet/minecraft/entity/ai/attributes/Attribute;)D   // remove

  ====== PATCH START ======
  INVOKESTATIC dev/toma/gunsrpg/asm/Hooks.modifyAttackDelay (Lnet/minecraft/entity/player/PlayerEntity;)D
  ====== PATCH END   ======

  DDIV
  LDC 20.0
  DMUL
  D2F
  FRETURN
 L1
  LOCALVARIABLE this Lnet/minecraft/entity/player/PlayerEntity; L0 L1 0
  MAXSTACK = 4
  MAXLOCALS = 1
*/
/**
 * Simple hook call which allows me to directly change attack speed values any time I need to rather
 * than waiting for equip event which is not fired in some occassions like equipping empty item or item of same type.
 * @param instructions List of instructions
 */
function patchAttackDelay(instructions) {
    for (var i = 0; i < instructions.size(); i++) {
        var instruction = instructions.get(i);
        if (instruction.getOpcode() === GETSTATIC) {
            var virtualInvoke = instructions.get(i + 1);
            var list = new InsnList();
            list.add(new MethodInsnNode(
                INVOKESTATIC,
                'dev/toma/gunsrpg/asm/Hooks',
                'modifyAttackDelay',
                '(Lnet/minecraft/entity/player/PlayerEntity;)D',
                false
            ));
            instructions.insertBefore(instruction, list);
            instructions.remove(instruction);
            instructions.remove(virtualInvoke);
            break;
        }
    }
}

/*
protected getFollowDistance()D
 L0
  LINENUMBER 74 L0
  ALOAD 0
  GETFIELD net/minecraft/entity/ai/goal/TargetGoal.mob : Lnet/minecraft/entity/MobEntity;

  ===== PATCH START =====
  INVOKESTATIC dev/toma/gunsrpg/asm/Hooks.modifyFollowDistance (Lnet/minecraft/entity/MobEntity;)D
  ===== PATCH END   =====

  GETSTATIC net/minecraft/entity/ai/attributes/Attributes.FOLLOW_RANGE : Lnet/minecraft/entity/ai/attributes/Attribute;   // remove
  INVOKEVIRTUAL net/minecraft/entity/MobEntity.getAttributeValue (Lnet/minecraft/entity/ai/attributes/Attribute;)D        // remove
  DRETURN
 L1
  LOCALVARIABLE this Lnet/minecraft/entity/ai/goal/TargetGoal; L0 L1 0
  MAXSTACK = 2
  MAXLOCALS = 1
*/
/**
 * Used to dynamically change follow distance of all mobs during bloodmoon.
 * This way is much faster rather than iterating over all mobs around players and updating their attributes
 * @param instructions Instruction list
 */
function patchFollowDistance(instructions) {
    for (var i = 0; i < instructions.size(); i++) {
        var instruction = instructions.get(i);
        if (instruction.getOpcode() === GETFIELD) {
            var methodInsert = new MethodInsnNode(
                INVOKESTATIC,
                'dev/toma/gunsrpg/asm/Hooks',
                'modifyFollowDistance',
                '(Lnet/minecraft/entity/MobEntity;)D',
                false
            );
            var getStaticInsn = instructions.get(i + 1);
            var invokeVirtualInsn = instructions.get(i + 2);
            instructions.insert(instruction, methodInsert);
            instructions.remove(getStaticInsn);
            instructions.remove(invokeVirtualInsn);
            break;
        }
    }
}

function patchBlockDrops(instructions) {
    var foundReturnStatement = false;
    for (var i = instructions.size() - 1; i >= 0; i--) {
        var instruction = instructions.get(i);
        if (!foundReturnStatement) {
            if (instruction.getOpcode() === ARETURN) {
                foundReturnStatement = true;
            }
        } else {
            if (instruction.getOpcode() === INVOKEVIRTUAL && instruction instanceof MethodInsnNode) {
                if (instruction.desc === '(Lnet/minecraft/loot/LootContext;)Ljava/util/List;') {
                    instructions.set(instruction, new MethodInsnNode(
                        INVOKESTATIC,
                        'dev/toma/gunsrpg/asm/Hooks',
                        'modifyBlockDrops',
                        '(Lnet/minecraft/loot/LootTable;Lnet/minecraft/loot/LootContext;)Ljava/util/List;',
                        false
                    ));
                    break;
                }
            }
        }
    }
}

/*
public <init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V
   L0
    LINENUMBER 87 L0
    ALOAD 0
    GETSTATIC net/minecraft/entity/EntityType.ENDER_DRAGON : Lnet/minecraft/entity/EntityType;
    ALOAD 2
    INVOKESPECIAL net/minecraft/entity/MobEntity.<init> (Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V
 */
function patchEnderDragonConstructor(instructions) {
    var superCall = -1;
    for (var i = 0; i < instructions.size(); i++) {
        var ins = instructions.get(i);
        if (ins.getOpcode() === INVOKESPECIAL) {
            superCall = i;
            break;
        }
    }
    if (superCall !== -1) {
        for (var j = superCall; j >= 0; j--) {
            var instruction = instructions.get(j);
            if (instruction.getOpcode() === GETSTATIC) {
                instructions.set(instruction, new VarInsnNode(ALOAD, 1));
                break;
            }
        }
    }
}

/*
public addFreshEntity(Lnet/minecraft/entity/Entity;)Z
   L0
    LINENUMBER 754 L0
    ALOAD 0
    ALOAD 1
    // INJECT START ---------------------
    -- REMOVE -- INVOKEVIRTUAL net/minecraft/world/server/ServerWorld.addEntity (Lnet/minecraft/entity/Entity;)Z
    -- ADD --    INVOKESTATIC dev/toma/gunsrpg/asm/Hooks.spawnEntity (Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/entity/Entity;)Z
    // INJECT END -----------------------
    IRETURN
   L1
    LOCALVARIABLE this Lnet/minecraft/world/server/ServerWorld; L0 L1 0
    LOCALVARIABLE p_217376_1_ Lnet/minecraft/entity/Entity; L0 L1 1
    MAXSTACK = 2
    MAXLOCALS = 2
*/
function addFreshEntitySpawnHook(instructions) {
    for (var i = 0; i < instructions.size(); i++) {
        var ins = instructions.get(i);
        if (ins.getOpcode() === INVOKEVIRTUAL) {
            instructions.set(ins, new MethodInsnNode(
                INVOKESTATIC,
                'dev/toma/gunsrpg/asm/Hooks',
                'spawnEntity',
                '(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/entity/Entity;)Z',
                false
            ));
            break;
        }
    }
}

function patchAmbientParticle(instructions) {
    for (var i = 0; i < instructions.size(); i++) {
        var ins = instructions.get(i);
        if (ins.getOpcode() === INVOKEVIRTUAL) {
            instructions.set(ins, new MethodInsnNode(
                INVOKESTATIC,
                'dev/toma/gunsrpg/asm/Hooks',
                'getAmbientParticle',
                '(Lnet/minecraft/world/biome/BiomeAmbience;)Ljava/util/Optional;',
                false
            ));
            break;
        }
    }
}

function patchAmbientLoopSound(instructions) {
    for (var i = 0; i < instructions.size(); i++) {
        var ins = instructions.get(i);
        if (ins.getOpcode() === INVOKEVIRTUAL) {
            instructions.set(ins, new MethodInsnNode(
                INVOKESTATIC,
                'dev/toma/gunsrpg/asm/Hooks',
                'getAmbientSoundLoop',
                '(Lnet/minecraft/world/biome/BiomeAmbience;)Ljava/util/Optional;',
                false
            ));
            break;
        }
    }
}

function patchAmbientMood(instructions) {
    for (var i = 0; i < instructions.size(); i++) {
        var ins = instructions.get(i);
        if (ins.getOpcode() === INVOKEVIRTUAL) {
            instructions.set(ins, new MethodInsnNode(
                INVOKESTATIC,
                'dev/toma/gunsrpg/asm/Hooks',
                'getAmbientMood',
                '(Lnet/minecraft/world/biome/BiomeAmbience;)Ljava/util/Optional;',
                false
            ));
            break;
        }
    }
}