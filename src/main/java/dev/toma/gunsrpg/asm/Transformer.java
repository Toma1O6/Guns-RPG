package dev.toma.gunsrpg.asm;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class Transformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        boolean obf = !name.equals(transformedName);
        if(transformedName.equals("net.minecraft.entity.EntityLivingBase")) {
            return patchEntityLivingBaseClass(basicClass, obf);
        }
        return basicClass;
    }

    private byte[] patchEntityLivingBaseClass(byte[] bytes, boolean obf) {
        GunsRPG.log.info("Attempting to patch EntityLivingBase class...");
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);
        Name onUpdateMethod = new Name("func_70071_h_", "onUpdate");
        String params = "()V";
        Name getAttributeModifiers = new Name("func_111283_C", "getAttributeModifiers");
        String getAttributeModifiersDesc = "(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lcom/google/common/collect/Multimap;";
        Name applyAttributeModifiers = new Name("func_111147_b", "applyAttributeModifiers");
        String applyAttributeModifiersParams = "(Lcom/google/common/collect/Multimap;)V";
        for(MethodNode methodNode : node.methods) {
            if(methodNode.name.equals(onUpdateMethod.get(obf)) && methodNode.desc.equals(params)) {
                InsnList insnList = methodNode.instructions;
                MethodInsnNode lastNode = null;
                for(AbstractInsnNode insnNode : insnList.toArray()) {
                    if(insnNode instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
                        if(methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                            if(lastNode != null && lastNode.name.equals(getAttributeModifiers.get(obf)) && lastNode.desc.equals(getAttributeModifiersDesc)) {
                                if(methodInsnNode.name.equals(applyAttributeModifiers.get(obf)) && methodInsnNode.desc.equals(applyAttributeModifiersParams)) {
                                    GunsRPG.log.info("Patching {} method in EntityLivingBase", onUpdateMethod.get(obf));
                                    InsnList insnList_inject = new InsnList();
                                    // "this" reference - in this case it's the current EntityLivingBase instance
                                    insnList_inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    // ItemStack which has been equipped
                                    insnList_inject.add(new VarInsnNode(Opcodes.ALOAD, 7));
                                    // Equipment slot which has detected change
                                    insnList_inject.add(new VarInsnNode(Opcodes.ALOAD, 5));
                                    String owner = "dev/toma/gunsrpg/asm/Hooks";
                                    String name = "dispatchApplyAttributesFromItemStack";
                                    String desc = "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/EntityEquipmentSlot;)V";
                                    MethodInsnNode invokeM = new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, false);
                                    insnList_inject.add(invokeM);
                                    insnList.insert(methodInsnNode, insnList_inject);
                                    GunsRPG.log.info("Patch successful");
                                    break;
                                }
                            }
                            lastNode = methodInsnNode;
                        }
                    }
                }
            }
        }
        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    private static class Name {

        private final String mcp, mapped;

        private Name(String mcp, String mapped) {
            this.mcp = mcp;
            this.mapped = mapped;
        }

        public String get(boolean obf) {
            return obf ? mcp : mapped;
        }
    }
}
