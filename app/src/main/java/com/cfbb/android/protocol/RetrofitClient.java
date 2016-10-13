package com.cfbb.android.protocol;

import android.app.Activity;
import android.content.Context;

import com.cfbb.android.BuildConfig;
import com.cfbb.android.commom.state.VertifyCodeEnum;
import com.cfbb.android.commom.utils.base.AppUtils;
import com.cfbb.android.commom.utils.base.NetworkUtils;
import com.cfbb.android.commom.utils.others.L;
import com.cfbb.android.commom.utils.others.MyJni;
import com.cfbb.android.commom.utils.others.StrUtil;
import com.cfbb.android.db.user.UserBiz;
import com.cfbb.android.protocol.bean.AccountDetailsInfoBean;
import com.cfbb.android.protocol.bean.AccountInfoBean;
import com.cfbb.android.protocol.bean.AccountInvestInfoBean;
import com.cfbb.android.protocol.bean.AccountLoanInfoBean;
import com.cfbb.android.protocol.bean.AccountSetInfoBean;
import com.cfbb.android.protocol.bean.AutoInvestInfoBean;
import com.cfbb.android.protocol.bean.BankBean;
import com.cfbb.android.protocol.bean.BaseResultBean;
import com.cfbb.android.protocol.bean.BidRecordBean;
import com.cfbb.android.protocol.bean.BindPhoneBean;
import com.cfbb.android.protocol.bean.BuyInitialBean;
import com.cfbb.android.protocol.bean.BuyRightBean;
import com.cfbb.android.protocol.bean.CertificationResultBean;
import com.cfbb.android.protocol.bean.HomeInfoBean;
import com.cfbb.android.protocol.bean.InComeBean;
import com.cfbb.android.protocol.bean.InvestInfoBean;
import com.cfbb.android.protocol.bean.MyBankInfoBean;
import com.cfbb.android.protocol.bean.MyGiftBean;
import com.cfbb.android.protocol.bean.MyInvestBean;
import com.cfbb.android.protocol.bean.MyInvestDetailsBean;
import com.cfbb.android.protocol.bean.MyLoanInfoBean;
import com.cfbb.android.protocol.bean.MyLoanListBean;
import com.cfbb.android.protocol.bean.MyRedPaperBean;
import com.cfbb.android.protocol.bean.PlanExplainBean;
import com.cfbb.android.protocol.bean.ProductBaseInfoBean;
import com.cfbb.android.protocol.bean.ProductInfoBean;
import com.cfbb.android.protocol.bean.ProductProjectInfoBean;
import com.cfbb.android.protocol.bean.ProductTypeBean;
import com.cfbb.android.protocol.bean.RechargeInfoBean;
import com.cfbb.android.protocol.bean.RechargeResultInfoBean;
import com.cfbb.android.protocol.bean.ServiceTermBean;
import com.cfbb.android.protocol.bean.TradeRecordBean;
import com.cfbb.android.protocol.bean.UnsupportedBankCardBean;
import com.cfbb.android.protocol.bean.UpdateVersionBean;
import com.cfbb.android.protocol.bean.UploadPhotoBean;
import com.cfbb.android.protocol.bean.UserBean;
import com.cfbb.android.protocol.bean.UserMsgBean;
import com.cfbb.android.protocol.bean.VertifyCodeInfoBean;
import com.cfbb.android.protocol.bean.VertifyInfoBean;
import com.cfbb.android.protocol.bean.WelcomeInfoBean;
import com.cfbb.android.protocol.bean.WithDrawInfoBean;
import com.cfbb.android.protocol.bean.WithdrawCashBean;
import com.cfbb.android.protocol.bean.WithdrawCheckBean;
import com.cfbb.android.protocol.bean.WithdrawRecordInfoBean;
import com.cfbb.android.protocol.bean.WithdrawRightInfoBean;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit.HttpException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author MrChang
 *         created  at  2016/2/24.
 * @description
 */
public class RetrofitClient {


    /**
     * 获取最新版本信息
     *
     * @param context
     * @param observer
     */
    public static Subscription getVersionInfoRequest(BaseResultBean<UpdateVersionBean> testResult, Context context, YCNetSubscriber<UpdateVersionBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("client_sys", "android");
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .UpdateVersionAPI(WrapParams(map, context)), context, observer);

    }


    /***
     * 登录
     *
     * @param userName
     * @param passWord
     * @param context
     * @param observer
     */
    public static Subscription loginRequest(BaseResultBean<UserBean> testResult, String userName, String passWord, Context context, YCNetSubscriber<UserBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("username", userName);
        map.put("password", passWord);
        return
                doRequest(testResult, RetrofitProxy
                        .getApiService(context)
                        .LoginAPI(WrapParams(map, context)), context, observer);

    }


    /***
     * 首页
     *
     * @param context
     * @param observer
     */
    public static Subscription getHomeRequest(BaseResultBean<HomeInfoBean> testResult, Context context, YCNetSubscriber<HomeInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetHome(WrapParams(map, context)), context, observer);

    }

    /***
     * 获得产品类型
     *
     * @param context
     * @param observer
     */
    public static Subscription getProductTypesRequest(BaseResultBean<List<ProductTypeBean>> testResult, Context context, YCNetSubscriber<List<ProductTypeBean>> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetProductType(WrapParams(map, context)), context, observer);

    }


    /***
     * 获得产品列表
     *
     * @param context
     * @param pageIndex     从1开始
     * @param productTypeId -1全部
     * @param sort          0创建时间 2利率 3期限
     * @param desc          0降序  1升序
     * @param observer
     */
    public static Subscription getProductListRequest(BaseResultBean<ProductInfoBean> testResult, Context context, int pageIndex, String productTypeId, String sort, String desc, String loan_typeId, YCNetSubscriber<ProductInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("page_index", pageIndex + "");
        map.put("product_typeId", productTypeId);
        map.put("sort_item", sort);
        map.put("is_desc", desc);
        map.put("loan_typeId", loan_typeId);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetProductList(WrapParams(map, context)), context, observer);
    }

    /***
     * 获得账户详情
     *
     * @param observer
     */
    public static Subscription getAccountInfo(BaseResultBean<AccountInfoBean> testResult, Context context, YCNetSubscriber<AccountInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetAccount(WrapParams(map, context)), context, observer);

    }

    /***
     * 获得产品 基本信息
     *
     * @param observer
     */
    public static Subscription geProductBaseInfo(BaseResultBean<ProductBaseInfoBean> testResult, String product_id, String loan_type_id, Context context, YCNetSubscriber<ProductBaseInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("product_id", product_id);
        map.put("loan_typeId", loan_type_id);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetProductBaseInfo(WrapParams(map, context)), context, observer);

    }

    /***
     * 获得产品 审核信息
     *
     * @param observer
     */
    public static Subscription getVertifyInfo(BaseResultBean<VertifyInfoBean> testResult, String product_id, Context context, YCNetSubscriber<VertifyInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("product_id", product_id);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetVertifyInfo(WrapParams(map, context)), context, observer);
    }

    /***
     * 获得产品 投标列表
     *
     * @param observer
     */
    public static Subscription getBidRecord(BaseResultBean<List<BidRecordBean>> testResult, String product_id, String pageIndex, Context context, YCNetSubscriber<List<BidRecordBean>> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("product_id", product_id);
        map.put("page_index", pageIndex);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetBidRecordInfo(WrapParams(map, context)), context, observer);

    }

    /***
     * 获得产品 项目信息
     *
     * @param observer
     */
    public static Subscription getProductProjectInfo(BaseResultBean<ProductProjectInfoBean> testResult, String product_id, Context context, YCNetSubscriber<ProductProjectInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("product_id", product_id);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetProductProjectInfo(WrapParams(map, context)), context, observer);
    }

    /***
     * 获得产品 计划说明
     *
     * @param observer
     */
    public static Subscription getPlanExplain(BaseResultBean<PlanExplainBean> testResult, String product_id, Context context, YCNetSubscriber<PlanExplainBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("product_id", product_id);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetPlanExplain(WrapParams(map, context)), context, observer);

    }


    /***
     * 获得账户设置信息
     *
     * @param observer
     */
    public static Subscription getAccountSetInfo(BaseResultBean<AccountSetInfoBean> testResult, Context context, YCNetSubscriber<AccountSetInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetAccountSetInfo(WrapParams(map, context)), context, observer);

    }

    /***
     * 实名认证
     *
     * @param observer
     */
    public static Subscription Certification(BaseResultBean testResult, String realName, String idCrad, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("real_name", realName);
        map.put("id_no", idCrad);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .Certification(WrapParams(map, context)), context, observer);
    }

    /***
     * 获得认证信息
     *
     * @param observer
     */
    public static Subscription getCertificationInfo(BaseResultBean<CertificationResultBean> testResult, Context context, YCNetSubscriber<CertificationResultBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .getCertificationInfo(WrapParams(map, context)), context, observer);
    }

    /***
     * 意见反馈
     *
     * @param observer
     */
    public static Subscription SubmitFeedBack(BaseResultBean testResult, String contents, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("contents", contents);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .SubmitFeedBack(WrapParams(map, context)), context, observer);
    }

    /***
     * 我的投资
     *
     * @param observer
     */
    public static Subscription MyInvest(BaseResultBean<List<MyInvestBean>> testResult, String type, String pageIndex, Context context, YCNetSubscriber<List<MyInvestBean>> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("bidding_type", type);
        map.put("page_index", pageIndex);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .MyInvest(WrapParams(map, context)), context, observer);

    }


    /***
     * 交易记录
     *
     * @param observer
     */
    public static Subscription GetTradeRecordList(BaseResultBean<List<TradeRecordBean>> testResult, String pageIndex, Context context, YCNetSubscriber<List<TradeRecordBean>> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("page_index", pageIndex);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .MyTradeRecordList(WrapParams(map, context)), context, observer);
    }


    /***
     * 账户明细 账户详情
     *
     * @param observer
     */
    public static Subscription GetMyAccountDetailsInfo(BaseResultBean<AccountDetailsInfoBean> testResult, Context context, YCNetSubscriber<AccountDetailsInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetMyAccountDetailsInfo(WrapParams(map, context)), context, observer);
    }


    /***
     * 账户明细 投资详情
     *
     * @param observer
     */
    public static Subscription GetMyAccountInvestInfo(BaseResultBean<AccountInvestInfoBean> testResult, Context context, YCNetSubscriber<AccountInvestInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetMyAccountInvestInfo(WrapParams(map, context)), context, observer);
    }

    /***
     * 我的银行卡
     *
     * @param observer
     */
    public static Subscription GetMyBankInfo(BaseResultBean<List<MyBankInfoBean>> testResult, Context context, YCNetSubscriber<List<MyBankInfoBean>> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetMyBankInfo(WrapParams(map, context)), context, observer);
    }


    /***
     * 我的红包
     *
     * @param observer
     */
    public static Subscription GetMyRedPaperList(BaseResultBean<List<MyRedPaperBean>> testResult, String page_index, Context context, YCNetSubscriber<List<MyRedPaperBean>> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("page_index", page_index);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetMyRedPaperList(WrapParams(map, context)), context, observer);
    }


    /***
     * 账户明细 借款详情
     *
     * @param observer
     */
    public static Subscription GetMyAccountLoanInfo(BaseResultBean<AccountLoanInfoBean> testResult, Context context, YCNetSubscriber<AccountLoanInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetMyAccountLoanInfo(WrapParams(map, context)), context, observer);
    }


    /***
     * 提现初始化
     *
     * @param observer
     */
    public static Subscription GetWithdrawInfo(BaseResultBean<WithDrawInfoBean> testResult, Context context, YCNetSubscriber<WithDrawInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetWithdrawInfo(WrapParams(map, context)), context, observer);
    }


    /***
     * 获取验证码
     *
     * @param observer
     */
    public static Subscription GeVertifyCode(BaseResultBean<VertifyCodeInfoBean> testResult, String mobile, String type, Context context, YCNetSubscriber<VertifyCodeInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        if (type.equals(VertifyCodeEnum.SET_NEW_PHONE_CODE.getValue())) {
            map.put(USER_ID, "");
        }
        map.put("mobile", mobile);
        map.put("type", type);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GeVertifyCode(WrapParams(map, context)), context, observer);
    }

    /***
     * 提交提现申请
     *
     * @param observer
     */
    public static Subscription SubmitWithdraw(BaseResultBean<WithdrawCashBean> testResult, String my_bank_id, String deposit_amount, String verCode, Context context, YCNetSubscriber<WithdrawCashBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("my_bank_id", my_bank_id);
        map.put("deposit_amount", deposit_amount);
        map.put("verCode", verCode);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .SubmitWithdraw(WrapParams(map, context)), context, observer);
    }


    /***
     * 提交成功
     *
     * @param observer
     */
    public static Subscription GetWithdrawRightInfo(BaseResultBean<WithdrawRightInfoBean> testResult, String withdrawCashId, Context context, YCNetSubscriber<WithdrawRightInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("withdraw_cash_id", withdrawCashId);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetWithdrawRightInfo(WrapParams(map, context)), context, observer);
    }


    /***
     * 提现记录
     *
     * @param observer
     */
    public static Subscription GetWithdrawRecordList(BaseResultBean<List<WithdrawRecordInfoBean>> testResult, String page_index, Context context, YCNetSubscriber<List<WithdrawRecordInfoBean>> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("page_index", page_index);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetWithdrawRecordList(WrapParams(map, context)), context, observer);
    }

    /***
     * 充值初始化
     *
     * @param observer
     */
    public static Subscription GetRechargeInitalInfo(BaseResultBean<RechargeInfoBean> testResult, Context context, YCNetSubscriber<RechargeInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetRechargeInitalInfo(WrapParams(map, context)), context, observer);
    }

    /***
     * 充值
     *
     * @param observer
     */
    public static Subscription Recarge(BaseResultBean<RechargeResultInfoBean> testResult, String no_agree, String amount, String bank_card_no,String selected_card, Context context, YCNetSubscriber<RechargeResultInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        if (StrUtil.isEmpty(no_agree)) {
            map.put("no_agree", "");
        } else {
            map.put("no_agree", no_agree);
        }
        if (StrUtil.isEmpty(bank_card_no)) {
            map.put("bank_card_no", "");
        } else {
            map.put("bank_card_no", bank_card_no);
        }
        map.put("amount", amount);
        //1 绑定卡 0 输入卡号
        map.put("selected_card", selected_card);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .RechargeSDK(WrapParams(map, context)), context, observer);
    }


    /***
     * 获取自动投标info
     *
     * @param observer
     */
    public static Subscription GetAutoInvestInfo(BaseResultBean<AutoInvestInfoBean> testResult, Context context, YCNetSubscriber<AutoInvestInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetAutoInvestInfo(WrapParams(map, context)), context, observer);

    }


    /***
     * 找回密码接口
     *
     * @param observer
     */
    public static Subscription FindPassWord(BaseResultBean testResult, String mobile, String code, String new_pwd, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("mobile", mobile);
        map.put("code", code);
        map.put("new_pwd", new_pwd);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .FindPassWord(WrapParams(map, context)), context, observer);

    }

    /***
     * 修改登录密码接口
     *
     * @param observer
     */
    public static Subscription ModifyPassWord(BaseResultBean testResult, String old_pwd, String new_pwd, String confirm_pwd, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("old_pwd", old_pwd);
        map.put("new_pwd", new_pwd);
        map.put("confirm_pwd", confirm_pwd);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .ModifyPassWord(WrapParams(map, context)), context, observer);
    }

    /***
     * 获取绑定手机号码接口
     *
     * @param observer
     */
    public static Subscription GetBindMobileInfo(BaseResultBean<BindPhoneBean> testResult, Context context, YCNetSubscriber<BindPhoneBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetBindMobileInfo(WrapParams(map, context)), context, observer);
    }

    /***
     * 修改原手机号
     *
     * @param observer
     */
    public static Subscription ModifyOriginalPhone(BaseResultBean testResult, String code, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();

        map.put("vercode", code);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .ModifyOriginalPhone(WrapParams(map, context)), context, observer);

    }


    /***
     * 身份验证接口
     *
     * @param observer
     */
    public static Subscription CheckIdentity(BaseResultBean testResult, String real_name, String id_num, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("real_name", real_name);
        map.put("id_no", id_num);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .CheckIdentity(WrapParams(map, context)), context, observer);

    }


    /***
     * 修改绑定手机接口
     *
     * @param observer
     */
    public static Subscription ChangeBindMobile(BaseResultBean testResult, String new_phone, String code, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("new_phone", new_phone);
        map.put("code", code);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .ChangeBindMobile(WrapParams(map, context)), context, observer);

    }

    /***
     * 注册接口
     *
     * @param observer
     */
    public static Subscription Register(BaseResultBean testResult, String username, String password, String mobile, String code, String referrer, String ip, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();

        map.put("username", username);
        map.put("password", password);
        map.put("mobile", mobile);
        map.put("verify_code", code);
        map.put("referrer", referrer);
        map.put("ip", ip);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .Register(WrapParams(map, context)), context, observer);

    }


    /***
     * 购买初始化接口
     *
     * @param observer
     */
    public static Subscription BuyInitial(BaseResultBean<BuyInitialBean> testResult, String loan_typeId, String product_id, Context context, YCNetSubscriber<BuyInitialBean> observer) {
        Map<String, String> map = new TreeMap<>();
        map.put("loan_typeId", loan_typeId);
        map.put("product_id", product_id);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .BuyInitial(WrapParams(map, context)), context, observer);


    }

    /***
     * 购买
     *
     * @param observer
     */
    public static Subscription InvestBid(BaseResultBean<InvestInfoBean> testResult, String product_id, String bid_money, Context context, YCNetSubscriber<InvestInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("product_id", product_id);
        map.put("bid_money", bid_money);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .InvestBid(WrapParams(map, context)), context, observer);

    }

    /***
     * 设置自动投标接口
     *
     * @param observer
     */
    public static Subscription SetAutoInvest(BaseResultBean testResult, String auto_type, String auto_amount, String rate_begin, String rate_end, String day_begin, String day_end, String repayMode_id, String is_open, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("auto_type", auto_type);
        map.put("auto_amount", auto_amount);
        map.put("rate_begin", rate_begin);
        map.put("rate_end", rate_end);
        map.put("day_begin", day_begin);
        map.put("day_end", day_end);
        map.put("repayMode_id", repayMode_id == "" ? "-1" : repayMode_id);
        map.put("is_open", is_open);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .SetAutoInvest(WrapParams(map, context)), context, observer);

    }


    /***
     * 设置自动投标开关接口
     *
     * @param observer
     */
    public static Subscription SetAutoInvestCloseOrOpen(BaseResultBean testResult, boolean is_open, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("is_open", is_open == true ? "1" : "-1");
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .SetAutoInvestCloseOrOpen(WrapParams(map, context)), context, observer);

    }

    /***
     * 使用红包
     *
     * @param observer
     */
    public static Subscription UseMyRedPaper(BaseResultBean testResult, String pocketId, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("packet_id", pocketId);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .UseMyRedPaper(WrapParams(map, context)), context, observer);
    }


    /***
     * 启动页广告
     *
     * @param observer
     */
    public static Subscription GetSplashInfo(BaseResultBean<WelcomeInfoBean> testResult, Context context, YCNetSubscriber<WelcomeInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetSplashInfo(WrapParams(map, context)), context, observer);
    }


    /***
     * 获取借款详情
     *
     * @param observer
     */
    public static Subscription GetMyLoanInfo(BaseResultBean<MyLoanInfoBean> testResult, Context context, YCNetSubscriber<MyLoanInfoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetMyLoanInfo(WrapParams(map, context)), context, observer);

    }

    /***
     * 添加借款
     *
     * @param observer
     */
    public static Subscription AddLoan(BaseResultBean testResult, String title, String amount, String term_type, String time, String rate, String repay_mode, String term, String area, String describe, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("title", title);
        map.put("amount", amount);
        map.put("term_type", term_type);
        map.put("bidding_days", time);
        map.put("rate", rate);
        map.put("repay_mode", repay_mode);
        map.put("term", term);
        map.put("area", area);
        map.put("describe", describe);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .AdDLoan(WrapParams(map, context)), context, observer);

    }

    /***
     * 获取我的借款列表
     *
     * @param observer
     */
    public static Subscription GetMyLoanList(BaseResultBean<List<MyLoanListBean>> testResult, Context context, YCNetSubscriber<List<MyLoanListBean>> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetMyLoanList(WrapParams(map, context)), context, observer);
    }


    /***
     * 上传头像
     *
     * @param observer
     */
    public static Subscription UploadPhoto(BaseResultBean<UploadPhotoBean> testResult, String avatar_content, String file_type_name, Context context, YCNetSubscriber<UploadPhotoBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("avatar_content", avatar_content);
        map.put("file_type_name", file_type_name);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .UploadPhoto(WrapParams(map, context)), context, observer);


    }

    /***
     * 购买成功
     *
     * @param observer
     */
    public static Subscription BuyRight(BaseResultBean<BuyRightBean> testResult, String invest_id, Context context, YCNetSubscriber<BuyRightBean> observer) {

        Map<String, String> map = new TreeMap<>();
        map.put("invest_id", invest_id);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .BuyRight(WrapParams(map, context)), context, observer);
    }

    /***
     * 收益计算
     *
     * @param observer
     */
    public static Subscription IncomeCalculation(BaseResultBean<InComeBean> testResult, String loan_type, String amount, String rate, String term, String term_type, Context context, YCNetSubscriber<InComeBean> observer) {

        Map<String, String> map = new TreeMap<>();

        map.put("loan_type", loan_type);
        map.put("amount", amount);
        map.put("rate", rate);
        map.put("term", term);
        map.put("term_type", term_type);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .IncomeCalculation(WrapParams(map, context)), context, observer);


    }


    /***
     * 我的投资详情
     *
     * @param observer
     */
    public static Subscription GetMyInvestDeatils(BaseResultBean<MyInvestDetailsBean> testResult, String bidding_type, String invest_id, Context context, YCNetSubscriber<MyInvestDetailsBean> observer) {
        Map<String, String> map = new TreeMap<>();
        map.put("invest_id", invest_id);
        map.put("bidding_type", bidding_type);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetMyInvestDeatils(WrapParams(map, context)), context, observer);


    }

    /***
     * 我的礼品
     *
     * @param observer
     */
    public static Subscription GetMyGift(BaseResultBean<List<MyGiftBean>> testResult, String curentIndex, Context context, YCNetSubscriber<List<MyGiftBean>> observer) {
        Map<String, String> map = new TreeMap<>();
        map.put("page_index", curentIndex);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetMyGifts(WrapParams(map, context)), context, observer);


    }


    /***
     * 删除银行卡
     *
     * @param observer
     */
    public static Subscription DeleteBank(BaseResultBean testResult, String my_bank_id, Context context, YCNetSubscriber observer) {
        Map<String, String> map = new TreeMap<>();
        map.put("my_bank_id", my_bank_id);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .DeleteBank(WrapParams(map, context)), context, observer);


    }

    /***
     * 获取注册服务条款
     *
     * @param observer
     */
    public static Subscription RegisterServiceTerm(BaseResultBean testResult, Context context, YCNetSubscriber<ServiceTermBean> observer) {
        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .RegisterServiceTerm(WrapParams(map, context)), context, observer);
    }

    /***
     * 添加银行卡
     *
     * @param observer
     */
    public static Subscription AddBank(BaseResultBean testResult, String bank_no,String bank_code, Context context, YCNetSubscriber observer) {
        Map<String, String> map = new TreeMap<>();
        map.put("bank_no", bank_no);
        map.put("bank_code", bank_code);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .AddBank(WrapParams(map, context)), context, observer);
    }


    /***
     * 添加银行卡
     *
     * @param observer
     */
    public static Subscription WithdrawCheck(BaseResultBean<WithdrawCheckBean> testResult, String withdraw_amount, Context context, YCNetSubscriber<WithdrawCheckBean> observer) {
        Map<String, String> map = new TreeMap<>();
        map.put("withdraw_amount", withdraw_amount);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .WithdrawCheck(WrapParams(map, context)), context, observer);
    }


    /***
     * 获取用户未读消息数目
     *
     * @param observer
     */
    public static Subscription GetUnReadMsgCount(BaseResultBean<UserMsgBean> testResult, Context context, YCNetSubscriber<UserMsgBean> observer) {
        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetUnReadMsgCount(WrapParams(map, context)), context, observer);
    }


    /***
     * 校验注册信息验证
     *
     * @param observer
     */
    public static Subscription CheckRegisterStep(BaseResultBean testResult, Context context, String userName, String password, String referrer, YCNetSubscriber observer) {
        Map<String, String> map = new TreeMap<>();
        map.put("username", userName);
        map.put("password", password);
        map.put("referrer", referrer);
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .CheckRegisterStep(WrapParams(map, context)), context, observer);
    }

    /***
     * 获取支持的银行卡列表
     *
     * @param observer
     */
    public static Subscription getSupportBankList(BaseResultBean<List<BankBean>> testResult, Context context, YCNetSubscriber<List<BankBean>> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .GetSupportBankList(WrapParams(map, context)), context, observer);
    }


    /***
     * 解绑不支持银行卡
     *
     * @param observer
     */
    public static Subscription UnBundlingNoSupportBank(BaseResultBean testResult, Context context, YCNetSubscriber observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .UnBundlingNoSupportBank(WrapParams(map, context)), context, observer);
    }
    /***
     * 是否存在不支持银行卡
     *
     * @param observer
     */
    public static Subscription IsExsitUnSupportBankCard(BaseResultBean<UnsupportedBankCardBean> testResult, Context context, YCNetSubscriber<UnsupportedBankCardBean> observer) {

        Map<String, String> map = new TreeMap<>();
        return doRequest(testResult, RetrofitProxy
                .getApiService(context)
                .IsExistUnSupportBankCard(WrapParams(map, context)), context, observer);
    }

    /***
     * 包装请求后再发起请求
     *
     * @param testResult 测试结果
     * @param observable 请求
     * @param context    上下文
     * @param observer   观察者回调
     * @param <T>
     * @return
     */
    private static <T> Subscription doRequest(BaseResultBean<T> testResult, Observable observable, Context context, YCNetSubscriber<T> observer) {
        return observable
                .onErrorReturn(new DealErroHttpFunc(context, testResult))
                .map(new HttpFurtherProcessingFunc(testResult))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    private static final String USER_ID = "user_id";

    /**
     * 包装参数
     *
     * @param params
     * @return
     */
    private static Map<String, String> WrapParams(Map<String, String> params, Context context) {

        if (params != null) {

            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String date = sDateFormat.format(new java.util.Date());

            boolean isContansUserId = false;
            String userId = "";
            List<String> listParams = new ArrayList<>();
            for (Map.Entry<String, String> temp : params.entrySet()) {
                listParams.add(temp.getKey() + "=" + temp.getValue());
                if (temp.getKey().equals(USER_ID)) {
                    isContansUserId = true;
                    userId = temp.getValue();
                }
            }
            listParams.add("time=" + date);
            listParams.add("app_sys=android");
            listParams.add("version_code=" + AppUtils.getAppInfo(context).getVersionCode());
            if (isContansUserId) {
                if (!StrUtil.isEmpty(userId)) {
                    listParams.add(USER_ID + "=" + userId);
                }
            } else {
                listParams.add(USER_ID + "=" + UserBiz.getInstance(context).GetUserId());
            }

            //public params
            params.put("time", date);
            params.put("app_sys", "android");
            params.put("version_code", AppUtils.getAppInfo(context).getVersionCode() + "");
            if (isContansUserId) {
                if (!StrUtil.isEmpty(userId)) {
                    params.put(USER_ID, userId);
                }
            } else {
                params.put(USER_ID, UserBiz.getInstance((Activity) context).GetUserId());
            }
            params.put("sign", CreateSign(listParams));

            return params;

        } else {

            return params;

        }

    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     * <p/>
     * 5001 返回的数据为null
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private static class HttpFurtherProcessingFunc<T> implements Func1<BaseResultBean<T>, T> {

        //代表自己写的测试数据
        private BaseResultBean<T> testDate;

        public HttpFurtherProcessingFunc(BaseResultBean<T> testDate) {
            this.testDate = testDate;
        }

        @Override
        public T call(BaseResultBean<T> httpResult) {

            if (BuildConfig.DEBUG) {

                if (testDate == null) {

                    if (httpResult == null) {
                       return (T) new BaseResultBean("测试数据和网络数据都为Null!", -99);
                    } else {
                        if (httpResult.code != APIService.OK_CODE) {
                            return (T) new BaseResultBean(httpResult.msg, httpResult.code);
                        } else {
                            return httpResult.data == null ? (T) new BaseResultBean("请求成功!", httpResult.code): httpResult.data ;
                        }
                    }


                } else {

                    if (testDate.code != APIService.OK_CODE) {
                        return (T) new BaseResultBean(testDate.msg, testDate.code);
                    }
                    return testDate.data == null ? (T) new BaseResultBean("请求成功!", testDate.code): testDate.data ;
                }

            } else {

                //Release版本
                if (null != httpResult) {
                    if (httpResult.code != APIService.OK_CODE) {
                        return (T) new BaseResultBean(httpResult.msg, httpResult.code);
                    }
                    return httpResult.data;
                } else {
                    return (T) new BaseResultBean("请求出现错误(5001)!", -99);
                }

            }
        }
    }


    /**
     * 用来统一处理测试数据和错误处理
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private static class DealErroHttpFunc<T extends BaseResultBean> implements Func1<Throwable, T> {

        private T testResult;
        private Context context;

        public DealErroHttpFunc(Context context, T t) {
            testResult = t;
            this.context = context;
        }

        @Override
        public T call(Throwable throwable) {

            if (BuildConfig.DEBUG) {
                L.e(throwable.getMessage());
                //在开发模式下，测试数据默认无视网络情况
                return testResult == null ? createException(throwable) : testResult;
            } else {
                //Release版本
                return createException(throwable);
            }
        }

        private T createException(Throwable throwable) {

            if (!NetworkUtils.hasNetWork(context)) {
                if (throwable instanceof SocketTimeoutException) {
                    return (T) new BaseResultBean("您的网络不给力！请重试！", -99);
                }
                if (throwable instanceof UnknownHostException || throwable instanceof HttpException) {
                    return (T) new BaseResultBean("在与服务器通讯过程中发生未知异常！", -99);
                }
                return (T) new BaseResultBean(throwable.getMessage()+"", -99);
            } else {
                return (T) new BaseResultBean("当前无法访问网络！", -99);
            }

        }
    }

    /***
     * using JNI to create sign
     *
     * @param data
     * @return
     */
    private static String CreateSign(List<String> data) {

        TreeMap<String, String> treeMap = new TreeMap<>();
        for (String item : data) {
            String[] temp = item.split("=");
            if (temp.length > 1) {
                treeMap.put(temp[0], temp[1]);
            } else {
                treeMap.put(temp[0], "");
            }
        }
        Collection col2 = treeMap.entrySet();
        Iterator it = col2.iterator();
        String temp = "";
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            temp += key + "=" + value + "&";
        }
        String sign = MyJni.getInstance().createSign(temp + MyJni.getInstance().getSecretStr());
        L.e("net", "params:" + temp + MyJni.getInstance().getSecretStr() + ",sign:" + sign);
        return sign;
    }

}
