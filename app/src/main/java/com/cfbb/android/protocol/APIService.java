package com.cfbb.android.protocol;

import com.cfbb.android.commom.utils.others.MyJni;
import com.cfbb.android.protocol.bean.AccountDetailsInfoBean;
import com.cfbb.android.protocol.bean.AccountInfoBean;
import com.cfbb.android.protocol.bean.AccountInvestInfoBean;
import com.cfbb.android.protocol.bean.AccountLoanInfoBean;
import com.cfbb.android.protocol.bean.AccountSetInfoBean;
import com.cfbb.android.protocol.bean.AuditStateBean;
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
import com.cfbb.android.protocol.bean.LoanInfoBean;
import com.cfbb.android.protocol.bean.LoanPersonInfo;
import com.cfbb.android.protocol.bean.LoanUrlBean;
import com.cfbb.android.protocol.bean.MyBankInfoBean;
import com.cfbb.android.protocol.bean.MyGiftBean;
import com.cfbb.android.protocol.bean.MyInvestBean;
import com.cfbb.android.protocol.bean.MyInvestDetailsBean;
import com.cfbb.android.protocol.bean.MyLoanBean;
import com.cfbb.android.protocol.bean.MyLoanDetailsBean;
import com.cfbb.android.protocol.bean.MyLoanInfoBean;
import com.cfbb.android.protocol.bean.MyRatesBean;
import com.cfbb.android.protocol.bean.MyRedPaperBean;
import com.cfbb.android.protocol.bean.PlanExplainBean;
import com.cfbb.android.protocol.bean.ProductBaseInfoBean;
import com.cfbb.android.protocol.bean.ProductInfoBean;
import com.cfbb.android.protocol.bean.ProductProjectInfoBean;
import com.cfbb.android.protocol.bean.ProductTypeBean;
import com.cfbb.android.protocol.bean.RatesBean;
import com.cfbb.android.protocol.bean.RechargeInfoBean;
import com.cfbb.android.protocol.bean.RechargeResultInfoBean;
import com.cfbb.android.protocol.bean.ServiceTermBean;
import com.cfbb.android.protocol.bean.ShareInfoBean;
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

import java.util.List;
import java.util.Map;

import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * @author MrChang
 *         created  at  2016/2/24.
 * @description
 */
public interface APIService {

    /***
     * 请求成功code
     */
    int OK_CODE = 1;
    /***
     * 未实名认证
     */
    int NO_AUTHENTICATION_CODE = 2;
    /***
     * 无绑定银行卡
     */
    int NO_BANKINFO_CODE = 4;

    /***
     * API host,后续url不要开头带/
     */
    String HOST = MyJni.getInstance().getHostAdd();

    /***
     * 版本更新
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("update_version")
    Observable<BaseResultBean<UpdateVersionBean>> UpdateVersionAPI(@FieldMap Map<String, String> params);

    /***
     * 登录
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Observable<BaseResultBean<UserBean>> LoginAPI(@FieldMap Map<String, String> params);

    /***
     * 首页接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("get_index")
    Observable<BaseResultBean<HomeInfoBean>> GetHome(@FieldMap Map<String, String> params);

    /***
     * 产品类型接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan_mode")
    Observable<BaseResultBean<List<ProductTypeBean>>> GetProductType(@FieldMap Map<String, String> params);

    /***
     * 标的列表接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/list")
    Observable<BaseResultBean<ProductInfoBean>> GetProductList(@FieldMap Map<String, String> params);


    /***
     * 我的账户接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/my_account")
    Observable<BaseResultBean<AccountInfoBean>> GetAccount(@FieldMap Map<String, String> params);

    /***
     * 产品详情 基本信息接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/detail")
    Observable<BaseResultBean<ProductBaseInfoBean>> GetProductBaseInfo(@FieldMap Map<String, String> params);

    /***
     * 产品详情 审核信息接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/examination")
    Observable<BaseResultBean<VertifyInfoBean>> GetVertifyInfo(@FieldMap Map<String, String> params);

    /***
     * 产品详情 投标列表接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/invest_records")
    Observable<BaseResultBean<List<BidRecordBean>>> GetBidRecordInfo(@FieldMap Map<String, String> params);

    /***
     * 产品详情 项目信息接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/borrower")
    Observable<BaseResultBean<ProductProjectInfoBean>> GetProductProjectInfo(@FieldMap Map<String, String> params);

    /***
     * 产品详情 计划说明接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/plan_desc")
    Observable<BaseResultBean<PlanExplainBean>> GetPlanExplain(@FieldMap Map<String, String> params);

    /***
     * 账户设置接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/my_account_settings")
    Observable<BaseResultBean<AccountSetInfoBean>> GetAccountSetInfo(@FieldMap Map<String, String> params);


    /***
     * 实名认证接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/legalize")
    Observable<BaseResultBean> Certification(@FieldMap Map<String, String> params);

    /***
     * 认证信息接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/legalized")
    Observable<BaseResultBean<CertificationResultBean>> getCertificationInfo(@FieldMap Map<String, String> params);

    /***
     * 意见反馈接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("opinions")
    Observable<BaseResultBean> SubmitFeedBack(@FieldMap Map<String, String> params);

    /***
     * 我的投资接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("account/biddingV2")
    Observable<BaseResultBean<List<MyInvestBean>>> MyInvest(@FieldMap Map<String, String> params);

    /***
     * 交易记录
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("account/money_recordV2")
    Observable<BaseResultBean<List<TradeRecordBean>>> MyTradeRecordList(@FieldMap Map<String, String> params);


    /***
     * 账户明细 账户详情
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("account/account_detailV2")
    Observable<BaseResultBean<AccountDetailsInfoBean>> GetMyAccountDetailsInfo(@FieldMap Map<String, String> params);

    /***
     * 账户明细 投资详情
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("account/invest_detail")
    Observable<BaseResultBean<AccountInvestInfoBean>> GetMyAccountInvestInfo(@FieldMap Map<String, String> params);

    /***
     * 账户明细 借款详情
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("account/about_loan_detail")
    Observable<BaseResultBean<AccountLoanInfoBean>> GetMyAccountLoanInfo(@FieldMap Map<String, String> params);

    /***
     * 我的银行卡
     *user/my_bank_card
     * my_bankCardV2
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/my_bankCardV3")
    Observable<BaseResultBean<List<MyBankInfoBean>>> GetMyBankInfo(@FieldMap Map<String, String> params);


    /***
     * 我的红包
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("account/red_packetV2")
    Observable<BaseResultBean<List<MyRedPaperBean>>> GetMyRedPaperList(@FieldMap Map<String, String> params);

    /***
     * 使用红包
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("account/use_redPacketV2")
    Observable<BaseResultBean> UseMyRedPaper(@FieldMap Map<String, String> params);

    /***
     * 提现初始化
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/get_withdraw_parsV2")
    Observable<BaseResultBean<WithDrawInfoBean>> GetWithdrawInfo(@FieldMap Map<String, String> params);


    /***
     * 获取验证码
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/get_captchaV2")
    Observable<BaseResultBean<VertifyCodeInfoBean>> GeVertifyCode(@FieldMap Map<String, String> params);

    /***
     * 提交提现申请
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/wealth_depositV2")
    Observable<BaseResultBean<WithdrawCashBean>> SubmitWithdraw(@FieldMap Map<String, String> params);


    /***
     * 提现提交成功
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/withdraw_success")
    Observable<BaseResultBean<WithdrawRightInfoBean>> GetWithdrawRightInfo(@FieldMap Map<String, String> params);


    /***
     * 提现记录
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/withdraw_logs")
    Observable<BaseResultBean<List<WithdrawRecordInfoBean>>> GetWithdrawRecordList(@FieldMap Map<String, String> params);


    /***
     * 充值初始化
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/init_recharge")
    Observable<BaseResultBean<RechargeInfoBean>> GetRechargeInitalInfo(@FieldMap Map<String, String> params);

    /***
     * 充值SDK
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/rechargeV2")
    Observable<BaseResultBean<RechargeResultInfoBean>> RechargeSDK(@FieldMap Map<String, String> params);


    /***
     * 获取自动投标设置
     *
     * @param params
     * @returnuser/recharge
     */
    @FormUrlEncoded
    @POST("user/get_autoInvestSettingV2")
    Observable<BaseResultBean<AutoInvestInfoBean>> GetAutoInvestInfo(@FieldMap Map<String, String> params);


    /***
     * 找回密码接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("retrieve_pwd")
    Observable<BaseResultBean> FindPassWord(@FieldMap Map<String, String> params);


    /***
     * 修改登录密码接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/modify_pwd")
    Observable<BaseResultBean> ModifyPassWord(@FieldMap Map<String, String> params);


    /***
     * 获取绑定手机号码接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/get_member_phone")
    Observable<BaseResultBean<BindPhoneBean>> GetBindMobileInfo(@FieldMap Map<String, String> params);

    /***
     * 修改原手机号接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/check_modify_phone")
    Observable<BaseResultBean> ModifyOriginalPhone(@FieldMap Map<String, String> params);


    /***
     * 身份验证接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/check_legalize")
    Observable<BaseResultBean> CheckIdentity(@FieldMap Map<String, String> params);

    /***
     * 修改绑定手机接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/set_new_phone")
    Observable<BaseResultBean> ChangeBindMobile(@FieldMap Map<String, String> params);

    /***
     * 注册接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("register")
    Observable<BaseResultBean> Register(@FieldMap Map<String, String> params);


    /***
     * 购买初始化接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/init_invest")
    Observable<BaseResultBean<BuyInitialBean>> BuyInitial(@FieldMap Map<String, String> params);

    /***
     * 购买
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/do_invest")
    Observable<BaseResultBean<InvestInfoBean>> InvestBid(@FieldMap Map<String, String> params);


    /***
     * 设置自动投标接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/autoInvest_setV2")
    Observable<BaseResultBean> SetAutoInvest(@FieldMap Map<String, String> params);

    /***
     * 设置自动投标开关接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/set_autoInvestFlg")
    Observable<BaseResultBean> SetAutoInvestCloseOrOpen(@FieldMap Map<String, String> params);

    /***
     * 启动广告
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("get_start_ads")
    Observable<BaseResultBean<WelcomeInfoBean>> GetSplashInfo(@FieldMap Map<String, String> params);


    /***
     * 获取借款详情
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("qdgg")
    Observable<BaseResultBean<MyLoanInfoBean>> GetMyLoanInfo(@FieldMap Map<String, String> params);


    /***
     * 添加借款
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/borrowing")
    Observable<BaseResultBean> AdDLoan(@FieldMap Map<String, String> params);


    /***
     * 获取我的借款列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("qdgg")
    Observable<BaseResultBean> GetMyLoanList(@FieldMap Map<String, String> params);

    /***
     * 上传头像
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/modify_avatar")
    Observable<BaseResultBean<UploadPhotoBean>> UploadPhoto(@FieldMap Map<String, String> params);


    /***
     * 购买成功接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("loan/invest_success")
    Observable<BaseResultBean<BuyRightBean>> BuyRight(@FieldMap Map<String, String> params);


    /***
     * 收益计算接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("calculat_interest")
    Observable<BaseResultBean<InComeBean>> IncomeCalculation(@FieldMap Map<String, String> params);

    /***
     * 我的投资详情接口
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("bidding/details")
    Observable<BaseResultBean<MyInvestDetailsBean>> GetMyInvestDeatils(@FieldMap Map<String, String> params);

    /***
     * 我的礼品
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("account/entity_presents")
    Observable<BaseResultBean<List<MyGiftBean>>> GetMyGifts(@FieldMap Map<String, String> params);


    /***
     * 删除银行卡
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/delete_bankV2")
    Observable<BaseResultBean> DeleteBank(@FieldMap Map<String, String> params);


    /***
     * 添加银行卡
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/add_bank_card")
    Observable<BaseResultBean> AddBank(@FieldMap Map<String, String> params);

    /***
     * 注册服务条款
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("register/service_terms")
    Observable<BaseResultBean<ServiceTermBean>> RegisterServiceTerm(@FieldMap Map<String, String> params);


    /***
     * 提现手续费提示框
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/withdraw_noticeV2")
    Observable<BaseResultBean<WithdrawCheckBean>> WithdrawCheck(@FieldMap Map<String, String> params);


    /***
     * 提现手续费提示框
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/unReadMsg_count")
    Observable<BaseResultBean<UserMsgBean>> GetUnReadMsgCount(@FieldMap Map<String, String> params);


    /***
     * 注册校验
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("check_register")
    Observable<BaseResultBean> CheckRegisterStep(@FieldMap Map<String, String> params);


    /***
     * 获取支持的银行卡列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/supported_bank_cards")
    Observable<BaseResultBean<List<BankBean>>> GetSupportBankList(@FieldMap Map<String, String> params);


    /***
     * 解绑不支持银行卡
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/baofoo_unbundling")
    Observable<BaseResultBean> UnBundlingNoSupportBank(@FieldMap Map<String, String> params);

    /***
     * 是否存在不支持银行卡
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/check_unsupported_bank_card")
    Observable<BaseResultBean<UnsupportedBankCardBean>> IsExistUnSupportBankCard(@FieldMap Map<String, String> params);

    /**
     * 获取借款列表信息
     * @param params
     * @return
     */

    @FormUrlEncoded
    @POST("/user/my_loan")
    Observable<BaseResultBean<MyLoanBean>> GetMyLoans(@FieldMap Map<String, String> params);

    /**
     * 获取借款详情
     * @param params
     * @return
     */

    @FormUrlEncoded
    @POST("/user/my_loandetails")
    Observable<BaseResultBean<MyLoanDetailsBean>> GetMyLoansDetails(@FieldMap Map<String, String> params);


    /**
     * 获取审核状态相关信息
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/loan_approvalstatus")
    Observable<BaseResultBean<AuditStateBean>> GetAuditState(@FieldMap Map<String, String> params);

    /**
     * 获取借款人信息
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/loan_info")
    Observable<BaseResultBean<LoanPersonInfo>> GetLoanPersonInfo(@FieldMap Map<String, String> params);

    /**
     * 获取借款人地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/loan/get_borrowLoan")
    Observable<BaseResultBean> GetLoanUrl(@FieldMap Map<String, String> params);

    /**
     * 获取分享信息
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/get_share_text")
    Observable<BaseResultBean<ShareInfoBean>> GetShareInfo(@FieldMap Map<String, String> params);

    /**
     * 我要借款
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/loan/borrowing")
    Observable<BaseResultBean<LoanInfoBean>> IntroduceLoan(@FieldMap Map<String, String> params);

    /**
     * 获取借款短信验证码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/loan/check_confirm_phone")
    Observable<BaseResultBean> GetSMS(@FieldMap Map<String, String> params);


    /**
     * 获取加息卷
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/loan/invest_interest")
    Observable<BaseResultBean<RatesBean>> GetRates(@FieldMap Map<String, String> params);


    /**
     * 获取我的加息卷
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/account/interest_coupon")
    Observable<BaseResultBean<List<MyRatesBean>>> GetMyRates(@FieldMap Map<String, String> params);
}
