/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.server.biometrics.sensors;

import android.content.Context;
import android.hardware.biometrics.BiometricConstants;
import android.hardware.biometrics.BiometricsProtoEnums;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.util.Slog;

import com.android.internal.util.FrameworkStatsLog;
import com.android.server.biometrics.Utils;

/**
 * Abstract class that adds logging functionality to the ClientMonitor classes.
 */
public abstract class LoggableMonitor {

    public static final String TAG = "Biometrics/LoggableMonitor";
    public static final boolean DEBUG = false;

    final int mStatsModality;
    private final int mStatsAction;
    private final int mStatsClient;
    private long mFirstAcquireTimeMs;

    /**
     * Only valid for AuthenticationClient.
     * @return true if the client is authenticating for a crypto operation.
     */
    protected boolean isCryptoOperation() {
        return false;
    }

    /**
     * @param statsModality One of {@link BiometricsProtoEnums} MODALITY_* constants.
     * @param statsAction One of {@link BiometricsProtoEnums} ACTION_* constants.
     * @param statsClient One of {@link BiometricsProtoEnums} CLIENT_* constants.
     */
    public LoggableMonitor(int statsModality, int statsAction, int statsClient) {
        mStatsModality = statsModality;
        mStatsAction = statsAction;
        mStatsClient = statsClient;
    }

    public int getStatsClient() {
        return mStatsClient;
    }

    private boolean isAnyFieldUnknown() {
        return mStatsModality == BiometricsProtoEnums.MODALITY_UNKNOWN
                || mStatsAction == BiometricsProtoEnums.ACTION_UNKNOWN
                || mStatsClient == BiometricsProtoEnums.CLIENT_UNKNOWN;
    }

    protected final void logOnAcquired(Context context, int acquiredInfo, int vendorCode,
            int targetUserId) {

        final boolean isFace = mStatsModality == BiometricsProtoEnums.MODALITY_FACE;
        final boolean isFingerprint = mStatsModality == BiometricsProtoEnums.MODALITY_FINGERPRINT;
        if (isFace || isFingerprint) {
            if ((isFingerprint && acquiredInfo == FingerprintManager.FINGERPRINT_ACQUIRED_START)
                    || (isFace && acquiredInfo == FaceManager.FACE_ACQUIRED_START)) {
                mFirstAcquireTimeMs = System.currentTimeMillis();
            }
        } else if (acquiredInfo == BiometricConstants.BIOMETRIC_ACQUIRED_GOOD) {
            if (mFirstAcquireTimeMs == 0) {
                mFirstAcquireTimeMs = System.currentTimeMillis();
            }
        }
        if (DEBUG) {
            Slog.v(TAG, "Acquired! Modality: " + mStatsModality
                    + ", User: " + targetUserId
                    + ", IsCrypto: " + isCryptoOperation()
                    + ", Action: " + mStatsAction
                    + ", Client: " + mStatsClient
                    + ", AcquiredInfo: " + acquiredInfo
                    + ", VendorCode: " + vendorCode);
        }

        if (isAnyFieldUnknown()) {
            return;
        }

        FrameworkStatsLog.write(FrameworkStatsLog.BIOMETRIC_ACQUIRED,
                mStatsModality,
                targetUserId,
                isCryptoOperation(),
                mStatsAction,
                mStatsClient,
                acquiredInfo,
                vendorCode,
                Utils.isDebugEnabled(context, targetUserId));
    }

    protected final void logOnError(Context context, int error, int vendorCode, int targetUserId) {

        final long latency = mFirstAcquireTimeMs != 0
                ? (System.currentTimeMillis() - mFirstAcquireTimeMs) : -1;

        if (DEBUG) {
            Slog.v(TAG, "Error! Modality: " + mStatsModality
                    + ", User: " + targetUserId
                    + ", IsCrypto: " + isCryptoOperation()
                    + ", Action: " + mStatsAction
                    + ", Client: " + mStatsClient
                    + ", Error: " + error
                    + ", VendorCode: " + vendorCode
                    + ", Latency: " + latency);
        } else {
            Slog.v(TAG, "Error latency: " + latency);
        }

        if (isAnyFieldUnknown()) {
            return;
        }

        FrameworkStatsLog.write(FrameworkStatsLog.BIOMETRIC_ERROR_OCCURRED,
                mStatsModality,
                targetUserId,
                isCryptoOperation(),
                mStatsAction,
                mStatsClient,
                error,
                vendorCode,
                Utils.isDebugEnabled(context, targetUserId),
                sanitizeLatency(latency));
    }

    protected final void logOnAuthenticated(Context context, boolean authenticated,
            boolean requireConfirmation, int targetUserId, boolean isBiometricPrompt) {
        int authState = FrameworkStatsLog.BIOMETRIC_AUTHENTICATED__STATE__UNKNOWN;
        if (!authenticated) {
            authState = FrameworkStatsLog.BIOMETRIC_AUTHENTICATED__STATE__REJECTED;
        } else {
            // Authenticated
            if (isBiometricPrompt && requireConfirmation) {
                authState = FrameworkStatsLog.BIOMETRIC_AUTHENTICATED__STATE__PENDING_CONFIRMATION;
            } else {
                authState = FrameworkStatsLog.BIOMETRIC_AUTHENTICATED__STATE__CONFIRMED;
            }
        }

        // Only valid if we have a first acquired time, otherwise set to -1
        final long latency = mFirstAcquireTimeMs != 0
                ? (System.currentTimeMillis() - mFirstAcquireTimeMs)
                : -1;

        if (DEBUG) {
            Slog.v(TAG, "Authenticated! Modality: " + mStatsModality
                    + ", User: " + targetUserId
                    + ", IsCrypto: " + isCryptoOperation()
                    + ", Client: " + mStatsClient
                    + ", RequireConfirmation: " + requireConfirmation
                    + ", State: " + authState
                    + ", Latency: " + latency);
        } else {
            Slog.v(TAG, "Authentication latency: " + latency);
        }

        if (isAnyFieldUnknown()) {
            return;
        }

        FrameworkStatsLog.write(FrameworkStatsLog.BIOMETRIC_AUTHENTICATED,
                mStatsModality,
                targetUserId,
                isCryptoOperation(),
                mStatsClient,
                requireConfirmation,
                authState,
                sanitizeLatency(latency),
                Utils.isDebugEnabled(context, targetUserId));
    }

    protected final void logOnEnrolled(int targetUserId, long latency, boolean enrollSuccessful) {
        if (DEBUG) {
            Slog.v(TAG, "Enrolled! Modality: " + mStatsModality
                    + ", User: " + targetUserId
                    + ", Client: " + mStatsClient
                    + ", Latency: " + latency
                    + ", Success: " + enrollSuccessful);
        } else {
            Slog.v(TAG, "Enroll latency: " + latency);
        }

        if (isAnyFieldUnknown()) {
            return;
        }

        FrameworkStatsLog.write(FrameworkStatsLog.BIOMETRIC_ENROLLED,
                mStatsModality,
                targetUserId,
                sanitizeLatency(latency),
                enrollSuccessful);
    }

    private long sanitizeLatency(long latency) {
        if (latency < 0) {
            Slog.w(TAG, "found a negative latency : " + latency);
            return -1;
        }
        return latency;
    }

}
