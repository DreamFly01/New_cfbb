package com.cfbb.android.db.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cfbb.android.commom.state.IsHidenMoneyShowEnum;
import com.cfbb.android.commom.state.IsOpenGestureEnum;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.commom.utils.secure.MyBase64;
import com.cfbb.android.commom.utils.secure.SecureUtils;
import com.cfbb.android.protocol.bean.UserBean;

/**
 * Created by MrChang45 on 2016/3/23.
 * 用户相关操作类
 */
public class UserBizHelper implements IUserBiz {

    private DbHelper mHelper;
    private SQLiteDatabase mDb;
    private ContentValues mValues;
    private Cursor mCursor;

    public UserBizHelper(Context context) {
        mHelper = new DbHelper(context);
        mDb = mHelper.getWritableDatabase();
    }

    public void StoreUserInfo(UserBean bean, String gesturePsw, int gestureState) {
        if (mDb != null) {
            mValues = new ContentValues();
            mValues.put(UserInfoTable.trueName, MyBase64.encode(SecureUtils.encryptMode(bean.userName.getBytes(), SecureUtils.Algorithm3DES)));
            mValues.put(UserInfoTable.isAuth, bean.isAuth);
            mValues.put(UserInfoTable.mobile, MyBase64.encode(SecureUtils.encryptMode(bean.mobile.getBytes(), SecureUtils.Algorithm3DES)));
            mValues.put(UserInfoTable.uid, MyBase64.encode(SecureUtils.encryptMode(bean.uid.getBytes(), SecureUtils.Algorithm3DES)));
            mValues.put(UserInfoTable.img, MyBase64.encode(SecureUtils.encryptMode(bean.img.getBytes(), SecureUtils.Algorithm3DES)));
            mValues.put(UserInfoTable.openEye, IsHidenMoneyShowEnum.SHOW.getValue());

            mValues.put(UserInfoTable.openGesture, gestureState);
            if (StrUtil.isEmpty(gesturePsw)) {
                mValues.put(UserInfoTable.gesturePassword, "");

            } else {
                mValues.put(UserInfoTable.gesturePassword, MyBase64.encode(SecureUtils.encryptMode(gesturePsw.getBytes(), SecureUtils.Algorithm3DES)));

            }
            mDb.insert(UserInfoTable.TABLE_NAME, UserInfoTable.gesturePassword, mValues);
        }
    }

    /**
     * 获得用户ID
     *
     * @return
     */
    public String GetUserId() {
        String userId = "";
        if (mDb != null) {
            mCursor = mDb.query(UserInfoTable.TABLE_NAME, new String[]{UserInfoTable.uid}, null, null,
                    null, null, null);
            if (mCursor.moveToNext()) {
                userId = new String(SecureUtils.decryptMode(MyBase64.decode(mCursor.getString(mCursor.getColumnIndex(UserInfoTable.uid))), SecureUtils.Algorithm3DES));
            }
        }
        return userId;
    }

    /***
     * 获得用户名
     *
     * @return
     */
    public String GetUserName() {
        String userName = "";
        if (mDb != null) {
            mCursor = mDb.query(UserInfoTable.TABLE_NAME, new String[]{UserInfoTable.trueName}, null,
                    null, null, null, null);
            if (mCursor.moveToNext()) {
                userName = mCursor.getString(mCursor.getColumnIndex(UserInfoTable.trueName));
            }

        }
        return new String(SecureUtils.decryptMode(MyBase64.decode(userName), SecureUtils.Algorithm3DES));
    }

    /***
     * 获得用户手机号码
     *
     * @return
     */
    public String GetUserMobile() {
        String userMobile = "";
        if (mDb != null) {
            mCursor = mDb.query(UserInfoTable.TABLE_NAME, new String[]{UserInfoTable.mobile}, null,
                    null, null, null, null);
            if (mCursor.moveToNext()) {
                userMobile = mCursor.getString(mCursor.getColumnIndex(UserInfoTable.mobile));
            }
        }
        return new String(SecureUtils.decryptMode(MyBase64.decode(userMobile), SecureUtils.Algorithm3DES));
    }

    /***
     * 获得用户头像地址
     *
     * @return
     */
    public String GetUserImgAddr() {
        String userImgAddr = "";
        if (mDb != null) {
            mCursor = mDb.query(UserInfoTable.TABLE_NAME, new String[]{UserInfoTable.img}, null,
                    null, null, null, null);
            if (mCursor.moveToNext()) {
                userImgAddr = mCursor.getString(mCursor.getColumnIndex(UserInfoTable.img));
                if (!StrUtil.isEmpty(userImgAddr)) {
                    userImgAddr = new String(SecureUtils.decryptMode(MyBase64.decode(userImgAddr), SecureUtils.Algorithm3DES));
                }
            }
        }
        return userImgAddr;
    }

    /***
     * 更新用户头像地址
     *
     * @return
     */
    public void UpdateUserImgAddr(String imgAddr) {
        if (mDb != null) {
            mValues = new ContentValues();
            if (StrUtil.isEmpty(imgAddr)) {
                mValues.put(UserInfoTable.img, imgAddr);
            } else {
                mValues.put(UserInfoTable.img, MyBase64.encode(SecureUtils.encryptMode(imgAddr.getBytes(), SecureUtils.Algorithm3DES)));
            }
            mDb.update(UserInfoTable.TABLE_NAME, mValues, null, null);
        }
    }


    /***
     * 获得用户手势密码
     *
     * @return
     */
    public String GetUserGesturePassword() {
        String gesturePassword = "";
        if (mDb != null) {
            mCursor = mDb.query(UserInfoTable.TABLE_NAME, new String[]{UserInfoTable.gesturePassword}, null,
                    null, null, null, null);
            if (mCursor.moveToNext()) {
                if (!StrUtil.isEmpty(mCursor.getString(mCursor.getColumnIndex(UserInfoTable.gesturePassword)))) {
                    gesturePassword = new String(SecureUtils.decryptMode(MyBase64.decode(mCursor.getString(mCursor.getColumnIndex(UserInfoTable.gesturePassword))), SecureUtils.Algorithm3DES));
                }
            }
        }
        return gesturePassword;
    }

    /***
     * 更新用户手势密码
     *
     * @param gesturePassword
     */
    public void UpdateUserGesturePassword(String gesturePassword) {
        if (mDb != null) {
            mValues = new ContentValues();
            mValues.put(UserInfoTable.gesturePassword, MyBase64.encode(SecureUtils.encryptMode(gesturePassword.getBytes(), SecureUtils.Algorithm3DES)));
            mValues.put(UserInfoTable.openGesture, IsOpenGestureEnum.OPEN.getValue());
            mDb.update(UserInfoTable.TABLE_NAME, mValues, null, null);
        }
    }


    /***
     * 更新用户资金显示状态
     *
     * @param state
     */
    public void UpdateUserMoenyShowState(int state) {
        if (mDb != null) {
            mValues = new ContentValues();
            mValues.put(UserInfoTable.openEye, state);
            mDb.update(UserInfoTable.TABLE_NAME, mValues, null, null);
        }
    }

    /***
     * 获得用户资金显示状态
     *
     * @return
     */
    public int Is_Show_Money() {
        int state = IsHidenMoneyShowEnum.SHOW.getValue();
        if (mDb != null) {
            mCursor = mDb.query(UserInfoTable.TABLE_NAME, new String[]{UserInfoTable.openEye}, null,
                    null, null, null, null);
            if (mCursor.moveToNext()) {
                state = mCursor.getInt(mCursor.getColumnIndex(UserInfoTable.openEye));
            }
        }
        return state;
    }

    /***
     * 是否设置手势密码
     *
     * @return
     */
    public String Is_Setted_Gesture() {
        String state = "";
        if (mDb != null) {
            mCursor = mDb.query(UserInfoTable.TABLE_NAME, new String[]{UserInfoTable.gesturePassword}, null,
                    null, null, null, null);
            if (mCursor.moveToNext()) {
                state = mCursor.getString(mCursor.getColumnIndex(UserInfoTable.gesturePassword));
            }
        }
        return state;
    }

    /***
     * 是否打开手势密码
     *
     * @return
     */
    public int Is_Open_Gesture() {
        int state = IsOpenGestureEnum.UN_KONW_STATE.getValue();
        if (mDb != null) {
            mCursor = mDb.query(UserInfoTable.TABLE_NAME, new String[]{UserInfoTable.openGesture}, null,
                    null, null, null, null);
            if (mCursor.moveToNext()) {
                state = mCursor.getInt(mCursor.getColumnIndex(UserInfoTable.openGesture));
            }
        }
        return state;
    }

    /***
     * 更新手势密码打开或关闭状态
     *
     * @param state
     */
    public void UpdateUserGestureState(int state) {
        if (mDb != null) {
            mValues = new ContentValues();
            mValues.put(UserInfoTable.openGesture, state);
            mDb.update(UserInfoTable.TABLE_NAME, mValues, null, null);
        }
    }


    /***
     * 清空用户表数据
     */
    public void CleanUserTable() {
        if (mDb != null) {
            mDb.delete(UserInfoTable.TABLE_NAME, null, null);
        }
    }

}
