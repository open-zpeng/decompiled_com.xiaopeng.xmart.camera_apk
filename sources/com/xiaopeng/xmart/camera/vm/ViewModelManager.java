package com.xiaopeng.xmart.camera.vm;

import com.xiaopeng.xmart.camera.vm.impl.AvmViewModel;
import java.util.HashMap;
/* loaded from: classes.dex */
public class ViewModelManager {
    private static final String TAG = "ViewModelManager";
    private final HashMap<Class<?>, IBaseViewModel> mViewModelsCache;

    /* loaded from: classes.dex */
    private static class SingleHolder {
        private static final ViewModelManager sInstance = new ViewModelManager();

        private SingleHolder() {
        }
    }

    public static ViewModelManager getInstance() {
        return SingleHolder.sInstance;
    }

    private ViewModelManager() {
        this.mViewModelsCache = new HashMap<>();
    }

    public <T extends IBaseViewModel> T getViewModelImpl(Class<?> clazz) throws IllegalArgumentException {
        synchronized (ViewModelManager.class) {
            T t = (T) this.mViewModelsCache.get(clazz);
            if (clazz.isInstance(t)) {
                return t;
            }
            T t2 = (T) createViewModel(clazz);
            this.mViewModelsCache.put(clazz, t2);
            return t2;
        }
    }

    private <T extends IBaseViewModel> T createViewModel(Class<?> modelClass) {
        if (modelClass == IAvmViewModel.class) {
            return new AvmViewModel();
        }
        throw new IllegalArgumentException("Unknown view model class: " + modelClass.getSimpleName());
    }
}
