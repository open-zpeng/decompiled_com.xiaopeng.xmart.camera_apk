package com.xiaopeng.xmart.camera.vm.impl;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.xiaopeng.xmart.camera.App;
import com.xiaopeng.xmart.camera.helper.CarCameraHelper;
import com.xiaopeng.xmart.camera.model.camera.CameraModel;
import com.xiaopeng.xmart.camera.model.camera.CarServiceControl;
import com.xiaopeng.xmart.camera.model.camera.IBaseCallback;
import com.xiaopeng.xmart.camera.model.camera.ICameraModel;
import com.xiaopeng.xmart.camera.model.camera.StartRecordMode;
import com.xiaopeng.xmart.camera.model.camera.TakePictureMode;
import com.xiaopeng.xmart.camera.utils.CameraLog;
import com.xiaopeng.xmart.camera.utils.ThreadPoolHelper;
import com.xiaopeng.xmart.camera.utils.ToastUtils;
import com.xiaopeng.xmart.camera.vm.IBaseViewModel;
import com.xiaopeng.xmart.camerabase.R;
/* loaded from: classes.dex */
public abstract class BaseViewModel<C extends CameraModel, T extends IBaseCallback> extends ViewModel implements IBaseViewModel, ICameraModel.Callback {
    private static final String TAG = "BaseViewModel";
    private String mCurPath;
    protected C mICameraModel;
    protected boolean mIsCameraOpen;
    private boolean mIsPreviewOpen;
    private boolean mIsRecording;
    private boolean mIsTakingPic;
    private String mRecordTime;
    protected MutableLiveData<Boolean> mCameraOpen = new MutableLiveData<>();
    protected MutableLiveData<Boolean> mCameraPreview = new MutableLiveData<>();
    protected MutableLiveData<TakePictureMode> mTakePicMode = new MutableLiveData<>();
    protected MutableLiveData<StartRecordMode> mStartRecordMode = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRecordTimeIsEvenNumber = new MutableLiveData<>();
    protected MutableLiveData<Boolean> mCameraRelease = new MutableLiveData<>();
    protected MutableLiveData<Boolean> mShowPreviewCover = new MutableLiveData<>();
    protected CarServiceControl mCarServiceControl = CarServiceControl.getInstance();
    public CameraModel.RecordTimeListener mRecordListener = new CameraModel.RecordTimeListener() { // from class: com.xiaopeng.xmart.camera.vm.impl.BaseViewModel.1
        @Override // com.xiaopeng.xmart.camera.model.camera.CameraModel.RecordTimeListener
        public void onTick(String timeText, boolean isEvenNumber) {
            CameraLog.d(BaseViewModel.TAG, "onTick, timeText: " + timeText + ", isEvenNumber: " + isEvenNumber, false);
            BaseViewModel.this.mRecordTime = timeText;
            if (BaseViewModel.this.mRecordTimeIsEvenNumber.hasActiveObservers()) {
                BaseViewModel.this.mRecordTimeIsEvenNumber.postValue(Boolean.valueOf(isEvenNumber));
            }
        }

        @Override // com.xiaopeng.xmart.camera.model.camera.CameraModel.RecordTimeListener
        public void onFinish() {
            CameraLog.d(BaseViewModel.TAG, "RecordTimeListener onFinish", false);
            BaseViewModel.this.stopRecord();
        }
    };

    public abstract void initModel();

    public void resetData() {
    }

    public BaseViewModel() {
        initModel();
    }

    public boolean isRecording() {
        return this.mIsRecording;
    }

    public boolean isCameraOpen() {
        return this.mIsCameraOpen;
    }

    public boolean isPreviewOpen() {
        return this.mIsPreviewOpen;
    }

    public boolean isTakingPic() {
        return this.mIsTakingPic;
    }

    public void openCamera(int cameraId) {
        CameraLog.d(TAG, "openCamera cameraId:" + cameraId, false);
        this.mRecordTime = "";
        this.mCameraRelease.postValue(false);
        C c = this.mICameraModel;
        if (c != null) {
            c.openCamera(cameraId);
        }
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void startPreview(SurfaceHolder holder) {
        CameraLog.d(TAG, "startPreview holder" + holder, false);
        C c = this.mICameraModel;
        if (c != null) {
            c.startPreview(holder);
        }
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void startPreview(SurfaceTexture surfaceTexture) {
        CameraLog.d(TAG, "startPreview surfaceTexture:" + surfaceTexture, false);
        C c = this.mICameraModel;
        if (c != null) {
            c.startPreview(surfaceTexture);
        }
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void stopPreview() {
        CameraLog.d(TAG, "stopPreview", false);
        C c = this.mICameraModel;
        if (c != null && this.mIsPreviewOpen) {
            c.stopPreview();
        }
        this.mIsPreviewOpen = false;
        setShowPreViewCover(true);
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void takePicture() {
        CameraLog.i(TAG, "takePicture isPreviewOpen:" + this.mIsPreviewOpen, false);
        if (this.mIsPreviewOpen) {
            this.mIsTakingPic = true;
            C c = this.mICameraModel;
            if (c != null) {
                c.takePicture();
            }
            this.mTakePicMode.postValue(TakePictureMode.Start);
        }
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void startRecord() {
        C c;
        CameraLog.d(TAG, "startRecord", false);
        if (!this.mIsPreviewOpen || (c = this.mICameraModel) == null) {
            return;
        }
        c.startRecord();
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void stopRecord() {
        CameraLog.d(TAG, "stopRecord", false);
        C c = this.mICameraModel;
        if (c != null) {
            c.stopRecord();
        }
    }

    public void closeCamera() {
        CameraLog.d(TAG, "closeCamera", false);
        C c = this.mICameraModel;
        if (c != null) {
            c.release();
        }
        this.mIsCameraOpen = false;
        this.mIsPreviewOpen = false;
        this.mIsTakingPic = false;
        this.mIsRecording = false;
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public void gotoGalleryDetail(String path) {
        C c = this.mICameraModel;
        if (c != null) {
            c.gotoGalleryDetail(path);
        }
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public String getLatestPicturePath() {
        C c = this.mICameraModel;
        if (c != null) {
            return c.getLatestPicturePath();
        }
        return null;
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public boolean isCurrentCameraThumbnail(String path) {
        return (this.mICameraModel == null || TextUtils.isEmpty(path) || !path.contains(this.mICameraModel.getFileDir())) ? false : true;
    }

    public void onCameraOpen(boolean openSuccess) {
        CameraLog.d(TAG, "onCameraOpen openSuccess:" + openSuccess, false);
        this.mCameraOpen.postValue(Boolean.valueOf(openSuccess));
        this.mIsCameraOpen = openSuccess;
        if (openSuccess) {
            return;
        }
        onCameraPreview(false);
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onCameraPreview(boolean previewSuccess) {
        CameraLog.d(TAG, "onCameraPreview previewSuccess:" + previewSuccess, false);
        this.mIsPreviewOpen = previewSuccess;
        this.mCameraPreview.postValue(Boolean.valueOf(previewSuccess));
        if (previewSuccess) {
            setShowPreViewCover(false);
        }
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onPictureTaken(String path) {
        CameraLog.d(TAG, "onPictureTaken path:" + path, false);
        this.mCurPath = path;
        this.mIsTakingPic = false;
        this.mTakePicMode.postValue(TakePictureMode.End);
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onRecordStart(boolean recordStart, String videoPath) {
        CameraLog.i(TAG, "onRecordStart recordStart:" + recordStart + ",videoPath:" + videoPath, false);
        this.mCurPath = videoPath;
        this.mIsRecording = recordStart;
        if (recordStart && this.mICameraModel != null) {
            ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.vm.impl.-$$Lambda$BaseViewModel$cJAR_yqA7HXIYhCvD8JVevgIYQU
                @Override // java.lang.Runnable
                public final void run() {
                    BaseViewModel.this.lambda$onRecordStart$0$BaseViewModel();
                }
            });
        }
        this.mStartRecordMode.postValue(recordStart ? StartRecordMode.Start : StartRecordMode.Fail);
    }

    public /* synthetic */ void lambda$onRecordStart$0$BaseViewModel() {
        this.mICameraModel.startDownCounter(this.mRecordListener);
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onRecordEnd(boolean recordEnd, final String videoPath) {
        CameraLog.i(TAG, "onRecordEnd recordEnd:" + recordEnd + ",videoPath:" + videoPath, false);
        this.mCurPath = videoPath;
        this.mIsRecording = false;
        this.mStartRecordMode.postValue(recordEnd ? StartRecordMode.End : StartRecordMode.Fail);
        C c = this.mICameraModel;
        if (c != null) {
            c.stopDownCounter();
        }
        if (!CarCameraHelper.getInstance().is3DProject()) {
            ThreadPoolHelper.getInstance().postOnMainThread(new Runnable() { // from class: com.xiaopeng.xmart.camera.vm.impl.-$$Lambda$BaseViewModel$So2Dl0apfUCJsua1dx9xQRuo3Rk
                @Override // java.lang.Runnable
                public final void run() {
                    BaseViewModel.lambda$onRecordEnd$1(videoPath);
                }
            });
        }
        this.mRecordTime = "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onRecordEnd$1(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ToastUtils.showToast(App.getInstance().getString(R.string.camera_record_finish));
    }

    @Override // com.xiaopeng.xmart.camera.model.camera.ICameraModel.Callback
    public void onCameraRelease() {
        CameraLog.d(TAG, "onCameraRelease", false);
        this.mIsCameraOpen = false;
        this.mIsPreviewOpen = false;
        this.mIsTakingPic = false;
        this.mIsRecording = false;
        this.mCameraRelease.postValue(true);
        this.mTakePicMode.postValue(TakePictureMode.Invalid);
        this.mStartRecordMode.postValue(StartRecordMode.Invalid);
    }

    public String getRecordTime() {
        return this.mRecordTime;
    }

    public String getCurPath() {
        return this.mCurPath;
    }

    @Override // com.xiaopeng.xmart.camera.vm.IBaseViewModel
    public Camera getCamera() {
        C c = this.mICameraModel;
        if (c == null) {
            this.mIsPreviewOpen = false;
            return null;
        }
        Camera camera = c.getCamera();
        if (camera == null) {
            CameraLog.i(TAG, "getCamera is null", false);
            this.mIsPreviewOpen = false;
            return null;
        }
        return camera;
    }

    public void setShowPreViewCover(boolean show) {
        this.mShowPreviewCover.postValue(Boolean.valueOf(show));
    }

    public boolean isShowPreviewCover() {
        MutableLiveData<Boolean> mutableLiveData = this.mCameraPreview;
        if (mutableLiveData == null || mutableLiveData.getValue() == null || this.mCameraPreview.getValue().booleanValue()) {
            MutableLiveData<Boolean> mutableLiveData2 = this.mShowPreviewCover;
            return (mutableLiveData2 == null || mutableLiveData2.getValue() == null || !this.mShowPreviewCover.getValue().booleanValue()) ? false : true;
        }
        return true;
    }

    public LiveData<Boolean> getShowPreviewCover() {
        return this.mShowPreviewCover;
    }

    public LiveData<TakePictureMode> getTakePicState() {
        return this.mTakePicMode;
    }

    public LiveData<StartRecordMode> getStartRecordState() {
        return this.mStartRecordMode;
    }

    public LiveData<Boolean> getCameraRelease() {
        return this.mCameraRelease;
    }

    public LiveData<Boolean> getRecordTimeIsEvenNumber() {
        return this.mRecordTimeIsEvenNumber;
    }

    public LiveData<Boolean> getCameraOpen() {
        return this.mCameraOpen;
    }

    public LiveData<Boolean> getCameraPreview() {
        return this.mCameraPreview;
    }
}
