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