// Initializes coremod
function initializeCoremod() {
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
        'attackDelayPatch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.player.PlayerEntity',
                'methodName': 'func_184818_cX',
                'methodDesc': '()F'
            },
            'transformer': function (methodNode) {
                patchAttackDelay(methodNode.instructions);
                return methodNode;
            }
        },

        'followDistancePatch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.ai.goal.TargetGoal',
                'methodName': 'func_111175_f',
                'methodDesc': '()D'
            },
            'transformer': function (methodNode) {
                patchFollowDistance(methodNode.instructions)
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
    for (let i = 0; i < instructions.size(); i++) {
        let instruction = instructions.get(i);
        if (instruction.getOpcode() === GETSTATIC) {
            let virtualInvoke = instructions.get(i + 1);
            let list = new InsnList();
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
    for (let i = 0; i < instructions.size(); i++) {
        let instruction = instructions.get(i);
        if (instruction.getOpcode() === GETFIELD) {
            let methodInsert = new MethodInsnNode(
                INVOKESTATIC,
                'dev/toma/gunsrpg/asm/Hooks',
                'modifyFollowDistance',
                '(Lnet/minecraft/entity/MobEntity;)D',
                false
            );
            let getStaticInsn = instructions.get(i + 1);
            let invokeVirtualInsn = instructions.get(i + 2);
            instructions.insert(instruction, methodInsert);
            instructions.remove(getStaticInsn);
            instructions.remove(invokeVirtualInsn);
            break;
        }
    }
}