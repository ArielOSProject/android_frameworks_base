/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.view.translation;

import android.annotation.IntDef;
import android.annotation.NonNull;
import android.annotation.SystemApi;
import android.os.Parcelable;

import com.android.internal.util.DataClass;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/**
 * Capability class holding information for a pair of {@link TranslationSpec}s.
 *
 * <p>Holds information and limitations on how to create a {@link TranslationContext} which can
 * be used by {@link TranslationManager#createTranslator(TranslationContext)}.
 */
@DataClass(genHiddenConstDefs = true, genToString = true, genConstructor = false)
public final class TranslationCapability implements Parcelable {

    /**
     * TODO: fill in javadoc
     */
    public static final @ModelState int STATE_AVAILABLE_TO_DOWNLOAD = 1;
    /**
     * TODO: fill in javadoc
     */
    public static final @ModelState int STATE_DOWNLOADING = 2;
    /**
     * TODO: fill in javadoc
     */
    public static final @ModelState int STATE_ON_DEVICE = 3;

    /**
     * The state of translation readiness between {@code mSourceSpec} and {@code mTargetSpec}.
     */
    private final @ModelState int mState;

    /**
     * {@link TranslationSpec} describing the source data specs for this
     * capability.
     */
    @NonNull
    private final TranslationSpec mSourceSpec;

    /**
     * {@link TranslationSpec} describing the target data specs for this
     * capability.
     */
    @NonNull
    private final TranslationSpec mTargetSpec;

    /**
     * Whether ui translation for the source-target {@link TranslationSpec}s is enabled.
     *
     * <p>Translation service will still support translation requests for this capability.</p>
     */
    private final boolean mUiTranslationEnabled;

    /**
     * Translation flags for settings that are supported by the
     * {@link android.service.translation.TranslationService} between the {@link TranslationSpec}s
     * provided in this capability.
     */
    private final @TranslationContext.TranslationFlag int mSupportedTranslationFlags;

    /**
     * Constructor for creating a {@link TranslationCapability}.
     *
     * @hide
     */
    @SystemApi
    public TranslationCapability(@ModelState int state, @NonNull TranslationSpec sourceSpec,
            @NonNull TranslationSpec targetSpec, boolean uiTranslationEnabled,
            @TranslationContext.TranslationFlag int supportedTranslationFlags) {
        Objects.requireNonNull(sourceSpec, "sourceSpec should not be null");
        Objects.requireNonNull(targetSpec, "targetSpec should not be null");

        this.mState = state;
        this.mSourceSpec = sourceSpec;
        this.mTargetSpec = targetSpec;
        this.mUiTranslationEnabled = uiTranslationEnabled;
        this.mSupportedTranslationFlags = supportedTranslationFlags;
    }



    // Code below generated by codegen v1.0.22.
    //
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    //
    // To regenerate run:
    // $ codegen $ANDROID_BUILD_TOP/frameworks/base/core/java/android/view/translation/TranslationCapability.java
    //
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    //   Settings > Editor > Code Style > Formatter Control
    //@formatter:off


    /** @hide */
    @IntDef(prefix = "STATE_", value = {
        STATE_AVAILABLE_TO_DOWNLOAD,
        STATE_DOWNLOADING,
        STATE_ON_DEVICE
    })
    @Retention(RetentionPolicy.SOURCE)
    @DataClass.Generated.Member
    public @interface ModelState {}

    /** @hide */
    @DataClass.Generated.Member
    public static String modelStateToString(@ModelState int value) {
        switch (value) {
            case STATE_AVAILABLE_TO_DOWNLOAD:
                    return "STATE_AVAILABLE_TO_DOWNLOAD";
            case STATE_DOWNLOADING:
                    return "STATE_DOWNLOADING";
            case STATE_ON_DEVICE:
                    return "STATE_ON_DEVICE";
            default: return Integer.toHexString(value);
        }
    }

    /**
     * The state of translation readiness between {@code mSourceSpec} and {@code mTargetSpec}.
     */
    @DataClass.Generated.Member
    public @ModelState int getState() {
        return mState;
    }

    /**
     * {@link TranslationSpec} describing the source data specs for this
     * capability.
     */
    @DataClass.Generated.Member
    public @NonNull TranslationSpec getSourceSpec() {
        return mSourceSpec;
    }

    /**
     * {@link TranslationSpec} describing the target data specs for this
     * capability.
     */
    @DataClass.Generated.Member
    public @NonNull TranslationSpec getTargetSpec() {
        return mTargetSpec;
    }

    /**
     * Whether ui translation for the source-target {@link TranslationSpec}s is enabled.
     *
     * <p>Translation service will still support translation requests for this capability.</p>
     */
    @DataClass.Generated.Member
    public boolean isUiTranslationEnabled() {
        return mUiTranslationEnabled;
    }

    /**
     * Translation flags for settings that are supported by the
     * {@link android.service.translation.TranslationService} between the {@link TranslationSpec}s
     * provided in this capability.
     */
    @DataClass.Generated.Member
    public @TranslationContext.TranslationFlag int getSupportedTranslationFlags() {
        return mSupportedTranslationFlags;
    }

    @Override
    @DataClass.Generated.Member
    public String toString() {
        // You can override field toString logic by defining methods like:
        // String fieldNameToString() { ... }

        return "TranslationCapability { " +
                "state = " + modelStateToString(mState) + ", " +
                "sourceSpec = " + mSourceSpec + ", " +
                "targetSpec = " + mTargetSpec + ", " +
                "uiTranslationEnabled = " + mUiTranslationEnabled + ", " +
                "supportedTranslationFlags = " + mSupportedTranslationFlags +
        " }";
    }

    @Override
    @DataClass.Generated.Member
    public void writeToParcel(@NonNull android.os.Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }

        byte flg = 0;
        if (mUiTranslationEnabled) flg |= 0x8;
        dest.writeByte(flg);
        dest.writeInt(mState);
        dest.writeTypedObject(mSourceSpec, flags);
        dest.writeTypedObject(mTargetSpec, flags);
        dest.writeInt(mSupportedTranslationFlags);
    }

    @Override
    @DataClass.Generated.Member
    public int describeContents() { return 0; }

    /** @hide */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    @DataClass.Generated.Member
    /* package-private */ TranslationCapability(@NonNull android.os.Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }

        byte flg = in.readByte();
        boolean uiTranslationEnabled = (flg & 0x8) != 0;
        int state = in.readInt();
        TranslationSpec sourceSpec = (TranslationSpec) in.readTypedObject(TranslationSpec.CREATOR);
        TranslationSpec targetSpec = (TranslationSpec) in.readTypedObject(TranslationSpec.CREATOR);
        int supportedTranslationFlags = in.readInt();

        this.mState = state;

        if (!(mState == STATE_AVAILABLE_TO_DOWNLOAD)
                && !(mState == STATE_DOWNLOADING)
                && !(mState == STATE_ON_DEVICE)) {
            throw new java.lang.IllegalArgumentException(
                    "state was " + mState + " but must be one of: "
                            + "STATE_AVAILABLE_TO_DOWNLOAD(" + STATE_AVAILABLE_TO_DOWNLOAD + "), "
                            + "STATE_DOWNLOADING(" + STATE_DOWNLOADING + "), "
                            + "STATE_ON_DEVICE(" + STATE_ON_DEVICE + ")");
        }

        this.mSourceSpec = sourceSpec;
        com.android.internal.util.AnnotationValidations.validate(
                NonNull.class, null, mSourceSpec);
        this.mTargetSpec = targetSpec;
        com.android.internal.util.AnnotationValidations.validate(
                NonNull.class, null, mTargetSpec);
        this.mUiTranslationEnabled = uiTranslationEnabled;
        this.mSupportedTranslationFlags = supportedTranslationFlags;
        com.android.internal.util.AnnotationValidations.validate(
                TranslationContext.TranslationFlag.class, null, mSupportedTranslationFlags);

        // onConstructed(); // You can define this method to get a callback
    }

    @DataClass.Generated.Member
    public static final @NonNull Parcelable.Creator<TranslationCapability> CREATOR
            = new Parcelable.Creator<TranslationCapability>() {
        @Override
        public TranslationCapability[] newArray(int size) {
            return new TranslationCapability[size];
        }

        @Override
        public TranslationCapability createFromParcel(@NonNull android.os.Parcel in) {
            return new TranslationCapability(in);
        }
    };

    @DataClass.Generated(
            time = 1616438309593L,
            codegenVersion = "1.0.22",
            sourceFile = "frameworks/base/core/java/android/view/translation/TranslationCapability.java",
            inputSignatures = "public static final @android.view.translation.TranslationCapability.ModelState int STATE_AVAILABLE_TO_DOWNLOAD\npublic static final @android.view.translation.TranslationCapability.ModelState int STATE_DOWNLOADING\npublic static final @android.view.translation.TranslationCapability.ModelState int STATE_ON_DEVICE\nprivate final @android.view.translation.TranslationCapability.ModelState int mState\nprivate final @android.annotation.NonNull android.view.translation.TranslationSpec mSourceSpec\nprivate final @android.annotation.NonNull android.view.translation.TranslationSpec mTargetSpec\nprivate final  boolean mUiTranslationEnabled\nprivate final @android.view.translation.TranslationContext.TranslationFlag int mSupportedTranslationFlags\nclass TranslationCapability extends java.lang.Object implements [android.os.Parcelable]\n@com.android.internal.util.DataClass(genHiddenConstDefs=true, genToString=true, genConstructor=false)")
    @Deprecated
    private void __metadata() {}


    //@formatter:on
    // End of generated code

}
