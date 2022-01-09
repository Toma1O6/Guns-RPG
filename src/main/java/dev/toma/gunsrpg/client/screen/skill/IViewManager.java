package dev.toma.gunsrpg.client.screen.skill;

public interface IViewManager {

    IViewContext getContext();

    View getView();

    void setView(View view);
}
