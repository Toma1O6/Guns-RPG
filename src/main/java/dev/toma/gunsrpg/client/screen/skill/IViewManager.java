package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.util.math.IDimensions;

public interface IViewManager {

    void init(View view);

    <T> void setView(int width, int height, T data, IViewFactory<?, T> factory);

    boolean isActive(View view);

    View getActive();

    class ViewManager implements IViewManager {

        private View active;
        private boolean flag;

        @Override
        public void init(View view) {
            active = view;
        }

        @Override
        public <T> void setView(int width, int height, T data, IViewFactory<?, T> factory) {
            active = factory.create(data, IDimensions.of(width, height));
        }

        @Override
        public boolean isActive(View view) {
            return view == active;
        }

        @Override
        public View getActive() {
            return active;
        }
    }
}
