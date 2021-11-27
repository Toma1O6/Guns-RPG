package dev.toma.gunsrpg.common.item.guns.util;

@FunctionalInterface
public interface IInputEventHandler {

    void invokeEvent(InputEventListenerType event);

    class Single implements IInputEventHandler {

        @Override
        public void invokeEvent(InputEventListenerType event) {

        }
    }

    class Burst implements IInputEventHandler {

        @Override
        public void invokeEvent(InputEventListenerType event) {

        }
    }

    class FullAuto implements IInputEventHandler {

        @Override
        public void invokeEvent(InputEventListenerType event) {

        }
    }

    class Barrage implements IInputEventHandler {

        @Override
        public void invokeEvent(InputEventListenerType event) {

        }
    }
}
