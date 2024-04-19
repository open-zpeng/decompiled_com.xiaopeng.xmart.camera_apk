package com.xiaopeng.xmart.camera;
/* loaded from: classes.dex */
public interface ICameraView {
    void changeTransparentVisible(boolean shouldShow);

    void hidePreviewCover();

    boolean isShowPreviewCover();

    void onAvmSwitchFail();

    void onAvmWorkStChanged(int state);

    void onCamera360Change();

    void onCameraError();

    void onCameraRelease();

    void onIgOff();

    default void onNEDCChanged(boolean state) {
    }

    void onPictureTaken(String path);

    void onRecordError();

    void onRecordStart();

    void onRecordStop(String videoPath);

    void onRecordTimeTick(String timeText, boolean isEvenNumber);

    void onSettingChanged(boolean isTransClassis, boolean isTransBody, boolean isBevMode);

    void onTakingPicture();

    void showPreviewCover(String msg);
}
