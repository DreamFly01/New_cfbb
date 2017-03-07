package com.cfbb.android.protocol;

import com.cfbb.android.commom.state.CertificationEnum;
import com.cfbb.android.commom.state.InvestPeriodEnum;
import com.cfbb.android.commom.state.InvestStateEnum;
import com.cfbb.android.commom.state.MoneyIsAddEnum;
import com.cfbb.android.commom.state.RechargeStateEnum;
import com.cfbb.android.commom.state.RedPaperStateEnum;
import com.cfbb.android.commom.state.RepaymentTypeEnum;
import com.cfbb.android.protocol.bean.AccountDetailsInfoBean;
import com.cfbb.android.protocol.bean.AccountInfoBean;
import com.cfbb.android.protocol.bean.AccountInvestInfoBean;
import com.cfbb.android.protocol.bean.AccountLoanInfoBean;
import com.cfbb.android.protocol.bean.AccountSetInfoBean;
import com.cfbb.android.protocol.bean.AuditStateBean;
import com.cfbb.android.protocol.bean.AutoInvestInfoBean;
import com.cfbb.android.protocol.bean.BaseResultBean;
import com.cfbb.android.protocol.bean.BidRecordBean;
import com.cfbb.android.protocol.bean.BindPhoneBean;
import com.cfbb.android.protocol.bean.BuyInitialBean;
import com.cfbb.android.protocol.bean.CertificationResultBean;
import com.cfbb.android.protocol.bean.HomeInfoBean;
import com.cfbb.android.protocol.bean.LoanPersonInfo;
import com.cfbb.android.protocol.bean.MyBankInfoBean;
import com.cfbb.android.protocol.bean.MyGiftBean;
import com.cfbb.android.protocol.bean.MyInvestBean;
import com.cfbb.android.protocol.bean.MyInvestCompleteBean;
import com.cfbb.android.protocol.bean.MyInvestHoldingBean;
import com.cfbb.android.protocol.bean.MyInvestingBean;
import com.cfbb.android.protocol.bean.MyLoanBean;
import com.cfbb.android.protocol.bean.MyLoanDetailsBean;
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
import com.cfbb.android.protocol.bean.TradeRecordBean;
import com.cfbb.android.protocol.bean.UpdateVersionBean;
import com.cfbb.android.protocol.bean.UserBean;
import com.cfbb.android.protocol.bean.VertifyCodeInfoBean;
import com.cfbb.android.protocol.bean.VertifyInfoBean;
import com.cfbb.android.protocol.bean.WelcomeInfoBean;
import com.cfbb.android.protocol.bean.WithDrawInfoBean;
import com.cfbb.android.protocol.bean.WithdrawRecordInfoBean;
import com.cfbb.android.protocol.bean.WithdrawRightInfoBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrChang45 on 2016/4/20.
 * <p/>
 * 测试数据
 */
public class TestResultUtils {


    public static BaseResultBean getBaseSussefulResult() {

        BaseResultBean baseResultBean = new BaseResultBean();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";

        return baseResultBean;
    }

    public static BaseResultBean getBaseFaliedResult() {

        BaseResultBean baseResultBean = new BaseResultBean();
        baseResultBean.code = -1;
        baseResultBean.msg = "登录成功";

        return baseResultBean;
    }

    public static BaseResultBean<AccountDetailsInfoBean> getSussefulResult2() {

        BaseResultBean<AccountDetailsInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        AccountDetailsInfoBean userBean = new AccountDetailsInfoBean();
        userBean.canUseTotal = "1000.00";
        userBean.freezeMoeny = "1000.00元";
        userBean.investFreezeMoney = "1000.00元";
        userBean.rechangeMoeny = "1000.00元";
        userBean.repentMoeny = "1000.00元";
        userBean.totalMoney = "1000.00";
        userBean.withDrawFreezeMoeny = "1000.00元";
        userBean.withDrawMoeny = "1000.00元";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<AccountInfoBean> getSussefulResult3() {
        BaseResultBean<AccountInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";

        AccountInfoBean accountInfoBean = new AccountInfoBean();

        AccountInfoBean accountInfo = new AccountInfoBean();
        accountInfo.accountTotalMoney = "100000.00";
        accountInfo.allreadyLoanMoeny = "1000.00";
        accountInfo.customerservice = "400-200-119";
        accountInfo.isRelease = 1;
        accountInfo.myRedPakageNum = "3个";
        accountInfo.totalEaring = "10000.00";
        accountInfo.userImageUrl = "http://www.apkbus.com/uc_server/avatar.php?uid=625970&size=middle";
        accountInfo.userName = "账务";
        accountInfo.workingTime = "9:00 -- 21:00";

        accountInfo.isShowHint = 1;
        accountInfo.turnWhere = 1;
        accountInfo.hintContent = "sdsdsdsdsd";

        baseResultBean.data = accountInfo;

        return baseResultBean;
    }

    public static BaseResultBean<AccountInvestInfoBean> getSussefulResult4() {
        BaseResultBean<AccountInvestInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        AccountInvestInfoBean userBean = new AccountInvestInfoBean();
        userBean.expectReturnMoeny = "10000.00";
        userBean.loanMoeny = "1000.00";
        userBean.recentDate = "10000.00元";
        userBean.returnInterest = "10000.00元";
        userBean.waitCapitalMoney = "10000.00";
        userBean.waitInterest = "1000.00";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<AccountLoanInfoBean> getSussefulResult5() {
        BaseResultBean<AccountLoanInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        AccountLoanInfoBean userBean = new AccountLoanInfoBean();
        userBean.expectReturnMoeny = "500.00元";
        userBean.loanMoney = "100.00";
        userBean.payInterest = "500.00元";
        userBean.recentReturnMoneyDate = "500.00元";
        userBean.returnMoeny = "100.00";
        userBean.waiMoeny = "500.00元";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<AccountSetInfoBean> getSussefulResult6() {
        BaseResultBean<AccountSetInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        AccountSetInfoBean userBean = new AccountSetInfoBean();
        userBean.isAuthentication = -1;
        userBean.isBind = -1;
        userBean.aboutUsUrl = "http://blog.csdn.net/t12x3456/article/details/39134961";
        userBean.helperCenterUrl = "http://blog.csdn.net/t12x3456/article/details/39134961";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<AutoInvestInfoBean> getSussefulResult7() {
        BaseResultBean<AutoInvestInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        AutoInvestInfoBean userBean = new AutoInvestInfoBean();
        userBean.auto_amount = "";
        userBean.auto_type = 1;
        userBean.day_begin = "30";
        userBean.day_end = "50";
        userBean.rate_begin = "8";
        userBean.rate_end = "12";
        userBean.is_open = 1;
        userBean.repayMode_id = "4,5,11";
        userBean.day_unit = InvestPeriodEnum.DAY.getValue();

        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean getSussefulResult8() {
        BaseResultBean baseResultBean = new BaseResultBean();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "认证成功";
        return baseResultBean;
    }

    public static BaseResultBean<List<BidRecordBean>> getSussefulResult9() {
        BaseResultBean<List<BidRecordBean>> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";

        BidRecordBean userBean = new BidRecordBean();

        List<BidRecordBean> bidInfoList = new ArrayList<>();
        BidRecordBean bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-01 12:12:12";
        bidInfo.money = "1000";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-012 12:12:12";
        bidInfo.money = "100";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-03 12:12:12";
        bidInfo.money = "10";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-04 12:12:12";
        bidInfo.money = "100";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-05 12:12:12";
        bidInfo.money = "10000";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-06 12:12:12";
        bidInfo.money = "1000";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-07 12:12:12";
        bidInfo.money = "100";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-08 12:12:12";
        bidInfo.money = "10000";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-09 12:12:12";
        bidInfo.money = "6000";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-01 12:12:12";
        bidInfo.money = "100400";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-01-01 12:12:12";
        bidInfo.money = "103000";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2014-11-01 12:12:12";
        bidInfo.money = "10000";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);
        bidInfo = new BidRecordBean();
        bidInfo.date = "2015-11-01 12:12:12";
        bidInfo.money = "10000";
        bidInfo.name = "刘xx";
        bidInfoList.add(bidInfo);

        baseResultBean.data = bidInfoList;
        return baseResultBean;
    }

    public static BaseResultBean<BindPhoneBean> getSussefulResult10() {
        BaseResultBean<BindPhoneBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        BindPhoneBean userBean = new BindPhoneBean();
        userBean.mobilePhone = "18888888888";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<BuyInitialBean> getSussefulResult11() {
        BaseResultBean<BuyInitialBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        BuyInitialBean userBean = new BuyInitialBean();
        userBean.accountAmount = "10000.00";
        userBean.loanFailingAmount = "500.00";
        userBean.termType = 1;
        userBean.term = "30";
        //userBean.repayment_type = RepaymentTypeEnum.DISPOSABLE.getValue();
        userBean.rate = "12.25";
        userBean.phoneNumLast4 = "5555";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean getSussefulResult12() {
        BaseResultBean baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";

        return baseResultBean;
    }


    public static BaseResultBean getSussefulResult13() {
        BaseResultBean baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        return baseResultBean;
    }

    public static BaseResultBean<CertificationResultBean> getSussefulResult14() {
        BaseResultBean<CertificationResultBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        CertificationResultBean userBean = new CertificationResultBean();
        userBean.realName = "于**";
        userBean.idCard = "**13442*******";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<HomeInfoBean> getSussefulResult15() {
        BaseResultBean<HomeInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "请求成功";
        HomeInfoBean homeInfoBean = new HomeInfoBean();

        //广告图
        HomeInfoBean.AdsBean adsBean = homeInfoBean.new AdsBean();
        adsBean.clickurl = "http://blog.csdn.net/finddreams/article/details/44301359";
        adsBean.imageUrl = "http://b.hiphotos.baidu.com/image/pic/item/d01373f082025aaf95bdf7e4f8edab64034f1a15.jpg";
        HomeInfoBean.AdsBean adsBean2 = homeInfoBean.new AdsBean();
        adsBean2.clickurl = "http://blog.csdn.net/finddreams/article/details/44301359";
        adsBean2.imageUrl = "http://g.hiphotos.baidu.com/image/pic/item/6159252dd42a2834da6660c459b5c9ea14cebf39.jpg";
        HomeInfoBean.AdsBean adsBean3 = homeInfoBean.new AdsBean();
        adsBean3.clickurl = "http://blog.csdn.net/finddreams/article/details/44301359";
        adsBean3.imageUrl = "http://g.hiphotos.baidu.com/image/pic/item/6159252dd42a2834da6660c459b5c9ea14cebf39.jpg";
        List<HomeInfoBean.AdsBean> ads = new ArrayList<>();
        ads.add(adsBean);
        ads.add(adsBean2);
        ads.add(adsBean3);
        homeInfoBean.ads = ads;

        homeInfoBean.otherHomeInfo = homeInfoBean.new otherHomeInfo();
        homeInfoBean.otherHomeInfo.helperUrl = "http://blog.csdn.net/finddreams/article/details/44301359";
        homeInfoBean.otherHomeInfo.messageUrl = "http://blog.csdn.net/finddreams/article/details/44301359";

        //咨询
        HomeInfoBean.NoticeBean noticeBean = homeInfoBean.new NoticeBean();
        noticeBean.clickurl = "http://blog.csdn.net/finddreams/article/details/44301359";
        noticeBean.content = "测试和谐的手机电视手机电视手机电视手机电视手机电视手机电视手机电视动画化决定ffdsfsdfg";
        HomeInfoBean.NoticeBean noticeBean2 = homeInfoBean.new NoticeBean();
        noticeBean2.clickurl = "http://blog.csdn.net/finddreams/article/details/44301359";
        noticeBean2.content = "测讲客家话离开家斯洛伐话离开家斯洛伐话离开家斯洛伐话离开家斯洛伐话离开家斯洛伐克首都浪费";
        HomeInfoBean.NoticeBean noticeBean3 = homeInfoBean.new NoticeBean();
        noticeBean3.clickurl = "http://blog.csdn.net/finddreams/article/details/44301359";
        noticeBean3.content = "发生发射点发射点发生发射点发射点风格发生发射点发射点发生发射点发射点风格发生发射点发射点发生发射点发射点风格发生发射点发射点发生发射点发射点风格发生发射点发射点发生发射点发射点风格";
        List<HomeInfoBean.NoticeBean> noticeBeens = new ArrayList<>();
        noticeBeens.add(noticeBean);
        noticeBeens.add(noticeBean2);
        noticeBeens.add(noticeBean3);
        homeInfoBean.notice = noticeBeens;


        HomeInfoBean.HotrecommendBean hotrecommendBean = homeInfoBean.new HotrecommendBean();
        hotrecommendBean.btntxt = "立即投资";
        hotrecommendBean.flagIconUrl = "";
        hotrecommendBean.investDate = "3";
        hotrecommendBean.investTatoalMoeny = "400000.00";
        hotrecommendBean.prodcutId = "1";
        hotrecommendBean.productName = "财富中国2014201548541";
        hotrecommendBean.loanTypeId = "1";
        hotrecommendBean.repaymentType = 11;
        hotrecommendBean.startMoeny = "100";
        hotrecommendBean.unit = 1;
        hotrecommendBean.yearOfRate = "12.25";
        hotrecommendBean.canInvest = 1;
        homeInfoBean.hotrecommend = hotrecommendBean;


        HomeInfoBean.OtherproductsBean otherproductsBean = homeInfoBean.new OtherproductsBean();
        otherproductsBean.btntxt = "立即投资";
        otherproductsBean.canInvest = 1;
        otherproductsBean.flagIconUrl = "";
        otherproductsBean.investDate = "2";
        otherproductsBean.prodcutId = "2";
        otherproductsBean.productName = "财富中国21152455212";
        otherproductsBean.loanTypeId = "1";
        otherproductsBean.prograss = "20.22";
        otherproductsBean.unit = 1;
        otherproductsBean.yearOfRate = "8.25";


        HomeInfoBean.OtherproductsBean otherproductsBean2 = homeInfoBean.new OtherproductsBean();
        otherproductsBean2.btntxt = "立即投资";
        otherproductsBean2.canInvest = 1;
        otherproductsBean2.flagIconUrl = "";
        otherproductsBean2.investDate = "2";
        otherproductsBean2.prodcutId = "2";
        otherproductsBean2.productName = "财富中国211524552152";
        otherproductsBean2.loanTypeId = "1";
        otherproductsBean2.prograss = "20.22";
        otherproductsBean2.unit = 1;
        otherproductsBean2.yearOfRate = "12.25";


        HomeInfoBean.OtherproductsBean otherproductsBean3 = homeInfoBean.new OtherproductsBean();
        otherproductsBean3.btntxt = "已结束";
        otherproductsBean3.canInvest = -1;
        otherproductsBean3.flagIconUrl = "";
        otherproductsBean3.investDate = "2";
        otherproductsBean3.prodcutId = "2";
        otherproductsBean3.productName = "财富中国s21152455212";
        otherproductsBean3.loanTypeId = "1";
        otherproductsBean3.prograss = "100.00";
        otherproductsBean3.unit = 1;
        otherproductsBean3.yearOfRate = "10.25";


        List<HomeInfoBean.OtherproductsBean> otherproductsBeanArrayList = new ArrayList<>();
        otherproductsBeanArrayList.add(otherproductsBean);
        otherproductsBeanArrayList.add(otherproductsBean2);
        otherproductsBeanArrayList.add(otherproductsBean3);

        homeInfoBean.otherproducts = otherproductsBeanArrayList;

        baseResultBean.data = homeInfoBean;
        return baseResultBean;
    }

    public static BaseResultBean<UserBean> getSussefulResult16() {
        BaseResultBean<UserBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        UserBean userBean = new UserBean();
        userBean.isAuth = CertificationEnum.PASS_CERTIFICATION.getValue();
        userBean.img = "http://www.apkbus.com/uc_server/avatar.php?uid=625970&size=middle";
        userBean.mobile = "14785245658";
        userBean.userName = "测试";
        userBean.uid = "xxxxx";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<MyBankInfoBean> getSussefulResult17() {
        BaseResultBean<MyBankInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        MyBankInfoBean userBean = new MyBankInfoBean();
        userBean.bankNum = "685125*******8888";
        userBean.imageUrl = "http://www.cfbb.com.cn/Content/Images/home/brands/icon-01.png";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<MyInvestCompleteBean> getSussefulResult18() {
        BaseResultBean<MyInvestCompleteBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        MyInvestCompleteBean investCompleteBean = new MyInvestCompleteBean();
        investCompleteBean.finishTime = "2015-01-01 12:12:12";
        investCompleteBean.interestReceived = "2000.00";
        investCompleteBean.investDate = "14";
        investCompleteBean.investMoeny = "123455.00";
        investCompleteBean.productName = "财富中国201501012585";
        investCompleteBean.repaymentType = RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getValue();
        investCompleteBean.unit = InvestPeriodEnum.DAY.getValue();
        investCompleteBean.yearOfRate = "12.25";

        baseResultBean.data = investCompleteBean;
        return baseResultBean;
    }

    public static BaseResultBean<MyInvestHoldingBean> getSussefulResult19() {
        BaseResultBean<MyInvestHoldingBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        MyInvestHoldingBean investCompleteBean = new MyInvestHoldingBean();
        investCompleteBean.buyTime = "2015-01-01 12:12:12";
        investCompleteBean.interestReceivable = "2000.00";
        investCompleteBean.investDate = "14";
        investCompleteBean.totalInvestMoeny = "123455.00";
        investCompleteBean.productName = "财富中国201501012585";
        investCompleteBean.repaymentType = RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getValue();
        investCompleteBean.unit = InvestPeriodEnum.DAY.getValue();
        investCompleteBean.yearOfRate = "12.25";
        investCompleteBean.currentYield = "2000.00";
        baseResultBean.data = investCompleteBean;
        return baseResultBean;
    }

    public static BaseResultBean<MyInvestingBean> getSussefulResult20() {
        BaseResultBean<MyInvestingBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        MyInvestingBean investCompleteBean = new MyInvestingBean();
        investCompleteBean.buyTime = "2015-01-01 12:12:12";
        investCompleteBean.interestReceivable = "2000.00";
        investCompleteBean.investDate = "14";
        investCompleteBean.totalInvestMoeny = "123455.00";
        investCompleteBean.productName = "财富中国201501012585";
        investCompleteBean.repaymentType = RepaymentTypeEnum.EQUAL_INSTALLMENT_PAYMENT.getValue();
        investCompleteBean.unit = InvestPeriodEnum.DAY.getValue();
        investCompleteBean.yearOfRate = "12.25";

        baseResultBean.data = investCompleteBean;
        return baseResultBean;
    }

    public static BaseResultBean<List<MyInvestBean>> getSussefulResult21() {
        BaseResultBean<List<MyInvestBean>> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        List<MyInvestBean> myInvestBeanList = new ArrayList<>();
        MyInvestBean myInvestBean = new MyInvestBean();

        myInvestBean.finishTime = "2015-1-01";
        myInvestBean.investMoeny = "10000.00";
        myInvestBean.productId = "1";
        myInvestBean.productName = "财富中国2015015555";
        myInvestBean.progress = "77";
        myInvestBean.remiansDay = "55";
        myInvestBean.state = 1;
        myInvestBean.totalEraning = "5000.00";
        myInvestBean.yearOfRate = "12.25";
        myInvestBeanList.add(myInvestBean);

        myInvestBean = new MyInvestBean();
        myInvestBean.finishTime = "2015-11-01";
        myInvestBean.investMoeny = "20000.00";
        myInvestBean.productId = "1";
        myInvestBean.productName = "财富中国2015asd555";
        myInvestBean.progress = "77";
        ;
        myInvestBean.remiansDay = "25";
        myInvestBean.state = 2;
        myInvestBean.totalEraning = "5000.00";
        myInvestBean.yearOfRate = "12.25";

        myInvestBean = new MyInvestBean();
        myInvestBeanList.add(myInvestBean);
        myInvestBean.finishTime = "2015-4-01";
        myInvestBean.investMoeny = "10300.00";
        myInvestBean.productId = "1";
        myInvestBean.productName = "财富中国2015ad5555";
        myInvestBean.progress = "77";
        myInvestBean.remiansDay = "55";
        myInvestBean.state = 3;
        myInvestBean.totalEraning = "5000.00";
        myInvestBean.yearOfRate = "11.25";
        myInvestBeanList.add(myInvestBean);

        myInvestBean = new MyInvestBean();
        myInvestBean.finishTime = "2015-1-01";
        myInvestBean.investMoeny = "10000.00";
        myInvestBean.productId = "1";
        myInvestBean.productName = "财富中国2015015555";
        myInvestBean.progress = "77";
        myInvestBean.remiansDay = "55";
        myInvestBean.state = 1;
        myInvestBean.totalEraning = "5000.00";
        myInvestBean.yearOfRate = "15.25";
        myInvestBeanList.add(myInvestBean);

        myInvestBean = new MyInvestBean();
        myInvestBean.finishTime = "2012-1-01";
        myInvestBean.investMoeny = "10300.00";
        myInvestBean.productId = "1";
        myInvestBean.productName = "财富中国2015015555";
        myInvestBean.progress = "77";
        myInvestBean.remiansDay = "55";
        myInvestBean.state = 2;
        myInvestBean.totalEraning = "5000.00";
        myInvestBean.yearOfRate = "12.25";
        myInvestBeanList.add(myInvestBean);

        myInvestBean = new MyInvestBean();
        myInvestBean.finishTime = "2015-1-01";
        myInvestBean.investMoeny = "10000.00";
        myInvestBean.productId = "1";
        myInvestBean.productName = "财富中国2015015555";
        myInvestBean.progress = "77";
        myInvestBean.remiansDay = "55";
        myInvestBean.state = 1;
        myInvestBean.totalEraning = "5000.00";
        myInvestBean.yearOfRate = "12.25";
        myInvestBeanList.add(myInvestBean);

        myInvestBean = new MyInvestBean();
        myInvestBean.finishTime = "2015-1-01";
        myInvestBean.investMoeny = "10000.00";
        myInvestBean.productId = "1";
        myInvestBean.productName = "财富中国2015015555";
        myInvestBean.progress = "77";
        myInvestBean.remiansDay = "55";
        myInvestBean.state = 3;
        myInvestBean.totalEraning = "6000.00";
        myInvestBean.yearOfRate = "11.25";
        myInvestBeanList.add(myInvestBean);

        baseResultBean.data = myInvestBeanList;
        return baseResultBean;
    }

    public static BaseResultBean<List<MyRedPaperBean>> getSussefulResult22() {
        BaseResultBean<List<MyRedPaperBean>> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";

        List<MyRedPaperBean> myRedPaperList = new ArrayList<>();
        MyRedPaperBean userBean = new MyRedPaperBean();
        userBean.from = "新手任务";
        userBean.money = "5.00";
        userBean.name = "现金卷";
        userBean.period = "2016-06-06";
        userBean.redPaperId = "222s";
        userBean.state = RedPaperStateEnum.OVERDUE_REDPAPER.getValue();
        myRedPaperList.add(userBean);


        userBean = new MyRedPaperBean();
        userBean.from = "新手任务新手任务";
        userBean.money = "15.00";
        userBean.name = "戴菁是卷";
        userBean.state = RedPaperStateEnum.OVERDUE_REDPAPER.getValue();
        userBean.period = "2016-06-06";
        userBean.redPaperId = "222s";
        myRedPaperList.add(userBean);

        userBean = new MyRedPaperBean();
        userBean.from = "新手任务";
        userBean.money = "15.00";
        userBean.name = "现金卷";
        userBean.state = RedPaperStateEnum.UNUSER_REDPAPER.getValue();
        userBean.period = "2016-06-06";
        userBean.redPaperId = "222s";
        myRedPaperList.add(userBean);

        userBean = new MyRedPaperBean();
        userBean.from = "新手任务新手任务";
        userBean.money = "5.00";
        userBean.state = RedPaperStateEnum.OVERDUE_REDPAPER.getValue();
        userBean.name = "现金卷";
        userBean.period = "2016-06-06";
        userBean.redPaperId = "222s";
        myRedPaperList.add(userBean);

        userBean = new MyRedPaperBean();
        userBean.from = "新手任务";
        userBean.money = "5.00";
        userBean.name = "现金卷";
        userBean.period = "2016-06-06";
        userBean.redPaperId = "222s";
        userBean.state = RedPaperStateEnum.OVERDUE_REDPAPER.getValue();
        myRedPaperList.add(userBean);

        userBean = new MyRedPaperBean();
        userBean.from = "新手任务新手任务";
        userBean.money = "5.00";
        userBean.name = "现金卷";
        userBean.period = "2016-06-06";
        userBean.state = RedPaperStateEnum.OVERDUE_REDPAPER.getValue();
        userBean.redPaperId = "222s";
        myRedPaperList.add(userBean);

        userBean = new MyRedPaperBean();
        userBean.from = "新手任务";
        userBean.money = "333.00";
        userBean.name = "现金卷";
        userBean.period = "2016-06-06";
        userBean.redPaperId = "222s";
        userBean.state = RedPaperStateEnum.OVERDUE_REDPAPER.getValue();
        myRedPaperList.add(userBean);

        userBean = new MyRedPaperBean();
        userBean.from = "新手任务";
        userBean.money = "5.00";
        userBean.name = "现金卷";
        userBean.period = "2016-06-06";
        userBean.redPaperId = "222s";
        userBean.state = RedPaperStateEnum.OVERDUE_REDPAPER.getValue();
        myRedPaperList.add(userBean);

        userBean = new MyRedPaperBean();
        userBean.from = "新手任务";
        userBean.money = "5.00";
        userBean.name = "现金卷";
        userBean.state = RedPaperStateEnum.UNUSER_REDPAPER.getValue();
        userBean.period = "2016-06-06";
        userBean.redPaperId = "222s";
        myRedPaperList.add(userBean);

        userBean = new MyRedPaperBean();
        userBean.from = "新手任务";
        userBean.money = "5.00";
        userBean.name = "现金卷";
        userBean.period = "2016-06-06";
        userBean.redPaperId = "222s";
        userBean.state = RedPaperStateEnum.OVERDUE_REDPAPER.getValue();
        myRedPaperList.add(userBean);

        baseResultBean.data = myRedPaperList;
        return baseResultBean;
    }


    public static BaseResultBean<PlanExplainBean> getSussefulResult23() {
        BaseResultBean<PlanExplainBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        PlanExplainBean userBean = new PlanExplainBean();
        userBean.url = "http://www.qq.com/";
        baseResultBean.data = userBean;
        return baseResultBean;
    }


    public static BaseResultBean<ProductBaseInfoBean> getSussefulResult24() {
        BaseResultBean<ProductBaseInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";

        ProductBaseInfoBean productBase = new ProductBaseInfoBean();
        productBase.assureCompany = "我担保我就是担保我控股毛线公司";
        productBase.investDate = "3";
        productBase.loanTotalMoney = "100000.00";
        productBase.productName = "财富中国20150214522";
        productBase.progress = "25.25";
        productBase.remiansDay = "154564565";
        productBase.repaymentType = 11;
        productBase.remiansMoeny = "1000.00";
        productBase.startMoney = "1000.00";
        productBase.unit = 3;
        productBase.yearOfRate = "12.25";
        productBase.investNum = "14笔";
        productBase.canInvest = InvestStateEnum.ENABLE_INVEST.getValue();
        productBase.btntxt = "立即投资";
        baseResultBean.data = productBase;
        return baseResultBean;
    }

    public static BaseResultBean<ProductInfoBean> getSussefulResult25() {
        BaseResultBean<ProductInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";

        ProductInfoBean productInfoBeanList = new ProductInfoBean();

        productInfoBeanList.showType = "房贷宝";
        ProductInfoBean.ProductInfo productBean = productInfoBeanList.new ProductInfo();

        productBean.btntxt = "立即投资";
        productBean.canInvest = 1;
        productBean.flagIcon = "1,2";
        productBean.investDate = "2";
        productBean.prodcutId = "1";
        productBean.productName = "财富中国2015021542";
        productBean.remiansMoeny = "10066660.00";
        productBean.unit = 1;
        productBean.loanTypeId = "3";
        productBean.yearOfRate = "12.25";
        productInfoBeanList.products.add(productBean);


        productBean = productInfoBeanList.new ProductInfo();
        productBean.btntxt = "立即投资";
        productBean.canInvest = 1;
        productBean.flagIcon = "1,2";
        productBean.investDate = "2";
        productBean.loanTypeId = "1";
        ;
        productBean.prodcutId = "2";
        productBean.productName = "财富中国2015021542";
        productBean.remiansMoeny = "1000.00";
        productBean.unit = 1;
        productBean.yearOfRate = "12.25";
        productInfoBeanList.products.add(productBean);


        productBean = productInfoBeanList.new ProductInfo();
        productBean.btntxt = "立即投资";
        productBean.canInvest = 1;
        productBean.flagIcon = "1,2";
        productBean.investDate = "2";
        productBean.loanTypeId = "3";
        ;
        productBean.prodcutId = "3";
        productBean.productName = "财富中国2015021542";
        productBean.remiansMoeny = "1000.00";
        productBean.unit = 1;
        productBean.yearOfRate = "12.25";
        productInfoBeanList.products.add(productBean);

        productBean = productInfoBeanList.new ProductInfo();
        productBean.btntxt = "立即投资";
        productBean.canInvest = 1;
        productBean.flagIcon = "1,2";
        productBean.loanTypeId = "3";
        ;
        productBean.investDate = "2";
        productBean.prodcutId = "4";
        productBean.productName = "财富中国2015021542";
        productBean.remiansMoeny = "1000.00";
        productBean.unit = 1;
        productBean.yearOfRate = "12.25";
        productInfoBeanList.products.add(productBean);


        productBean = productInfoBeanList.new ProductInfo();
        productBean.btntxt = "立即投资";
        productBean.canInvest = 1;
        productBean.flagIcon = "1,2";
        productBean.loanTypeId = "3";
        productBean.investDate = "2";
        productBean.prodcutId = "5";
        productBean.productName = "财富中国2015021542";
        productBean.remiansMoeny = "8888.00";
        productBean.unit = 1;
        productBean.yearOfRate = "12.25";
        productInfoBeanList.products.add(productBean);


        productBean = productInfoBeanList.new ProductInfo();
        productBean.btntxt = "立即投资";
        productBean.canInvest = 1;
        productBean.flagIcon = "1,2";
        productBean.loanTypeId = "3";
        productBean.investDate = "2";
        productBean.prodcutId = "6";
        productBean.productName = "财富中国2015021542";
        productBean.remiansMoeny = "10000000.00";
        productBean.unit = 1;
        productBean.yearOfRate = "12.25";
        productInfoBeanList.products.add(productBean);


        productBean = productInfoBeanList.new ProductInfo();
        productBean.btntxt = "立即投资";
        productBean.canInvest = 1;
        productBean.loanTypeId = "1";
        ;
        productBean.flagIcon = "1,2";
        productBean.investDate = "2";
        productBean.prodcutId = "7";
        productBean.productName = "财富中国2015021542";
        productBean.remiansMoeny = "1000.00";
        productBean.unit = 1;
        productBean.yearOfRate = "12.25";
        productInfoBeanList.products.add(productBean);

//
//        productBean = productInfoBean.new ProductBean();
//        productBean.btntxt="立即投资";
//        productBean.canInvest=1;
//        productBean.flagIcon="1,2";
//        productBean.investDate="2";
//        productBean.prodcutId="8";
//        productBean.productName="财富中国2015021542";
//        productBean.remiansMoeny="1000.00";
//        productBean.unit=1;
//        productBean.yearOfRate="12.25";
//        productInfoBeanList.add(productBean);
//
//
//
//        productBean = productInfoBean.new ProductBean();
//        productBean.btntxt="立即投资";
//        productBean.canInvest=1;
//        productBean.flagIcon="1,2";
//        productBean.investDate="2";
//        productBean.prodcutId="9";
//        productBean.productName="财富中国2015021542";
//        productBean.remiansMoeny="1000.00";
//        productBean.unit=1;
//        productBean.yearOfRate="12.25";
//        productInfoBeanList.add(productBean);
//
//
//
//        productBean = productInfoBean.new ProductBean();
//        productBean.btntxt="立即投资";
//        productBean.canInvest=1;
//        productBean.flagIcon="1,2";
//        productBean.investDate="2";
//        productBean.prodcutId="10";
//        productBean.productName="财富中国2015021542";
//        productBean.remiansMoeny="1000.00";
//        productBean.unit=1;
//        productBean.yearOfRate="12.25";
//        productInfoBeanList.add(productBean);
//
//
//
//        productBean = productInfoBean.new ProductBean();
//        productBean.btntxt="立即投资";
//        productBean.canInvest=1;
//        productBean.flagIcon="1,2";
//        productBean.investDate="2";
//        productBean.prodcutId="11";
//        productBean.productName="财富中国2015021542";
//        productBean.remiansMoeny="1000.00";
//        productBean.unit=1;
//        productBean.yearOfRate="12.25";
//        productInfoBeanList.add(productBean);
//
//
//
//        productBean = productInfoBean.new ProductBean();
//        productBean.btntxt="立即投资";
//        productBean.canInvest=1;
//        productBean.flagIcon="1,2";
//        productBean.investDate="2";
//        productBean.prodcutId="12";
//        productBean.productName="财富中国2015021542";
//        productBean.remiansMoeny="1000.00";
//        productBean.unit=1;
//        productBean.yearOfRate="12.25";
//        productInfoBeanList.add(productBean);
//
//
//
//        productBean = productInfoBean.new ProductBean();
//        productBean.btntxt="立即投资";
//        productBean.canInvest=1;
//        productBean.flagIcon="1,2";
//        productBean.investDate="2";
//        productBean.prodcutId="13";
//        productBean.productName="财富中国2015021542";
//        productBean.remiansMoeny="1000.00";
//        productBean.unit=1;
//        productBean.yearOfRate="12.25";
//        productInfoBeanList.add(productBean);
//
//        productBean = productInfoBean.new ProductBean();
//        productBean.btntxt="立即投资";
//        productBean.canInvest=1;
//        productBean.flagIcon="1,2";
//        productBean.investDate="2";
//        productBean.prodcutId="14";
//        productBean.productName="财富中国2015021542";
//        productBean.remiansMoeny="1000.00";
//        productBean.unit=1;
//        productBean.yearOfRate="12.25";
//        productInfoBeanList.add(productBean);
//
//
//        productBean = productInfoBean.new ProductBean();
//        productBean.btntxt="立即投资";
//        productBean.canInvest=1;
//        productBean.flagIcon="1,2";
//        productBean.investDate="2";
//        productBean.prodcutId="15";
//        productBean.productName="财富中国2015021542";
//        productBean.remiansMoeny="10000.00";
//        productBean.unit=1;
//        productBean.yearOfRate="12.25";
//        productInfoBeanList.add(productBean);

        baseResultBean.data = productInfoBeanList;
        return baseResultBean;
    }


    public static BaseResultBean<List<ProductTypeBean>> getSussefulResult26() {

        BaseResultBean<List<ProductTypeBean>> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        List<ProductTypeBean> prouctBeanList = new ArrayList<>();
        ProductTypeBean prouctBean = new ProductTypeBean();
        prouctBean.prodcutTypeId = "1";
        prouctBean.productTypeName = "房贷宝";
        prouctBean.loanTypeId = 1;
        prouctBeanList.add(prouctBean);

        ProductTypeBean prouctBean2 = new ProductTypeBean();
        prouctBean2.prodcutTypeId = "2";
        prouctBean2.productTypeName = "车贷宝";
        prouctBean2.loanTypeId = 1;
        prouctBeanList.add(prouctBean2);

        prouctBean2 = new ProductTypeBean();
        prouctBean2.prodcutTypeId = "2";
        prouctBean2.productTypeName = "车贷宝";
        prouctBean2.loanTypeId = 1;
        prouctBeanList.add(prouctBean2);

        ProductTypeBean prouctBean3 = new ProductTypeBean();
        prouctBean3.prodcutTypeId = "4";
        prouctBean3.productTypeName = "优质计划优质计划";
        prouctBean3.loanTypeId = 5;
        prouctBeanList.add(prouctBean3);

        prouctBean2 = new ProductTypeBean();
        prouctBean2.prodcutTypeId = "2";
        prouctBean2.productTypeName = "车贷宝";
        prouctBean2.loanTypeId = 1;
        prouctBeanList.add(prouctBean2);

        prouctBean2 = new ProductTypeBean();
        prouctBean2.prodcutTypeId = "2";
        prouctBean2.productTypeName = "车贷宝";
        prouctBean2.loanTypeId = 1;
        prouctBeanList.add(prouctBean2);

        prouctBean3 = new ProductTypeBean();
        prouctBean3.prodcutTypeId = "4";
        prouctBean3.productTypeName = "优质计划优质计划";
        prouctBean3.loanTypeId = 5;
        prouctBeanList.add(prouctBean3);

        prouctBean2 = new ProductTypeBean();
        prouctBean2.prodcutTypeId = "2";
        prouctBean2.productTypeName = "车贷宝";
        prouctBean2.loanTypeId = 1;
        prouctBeanList.add(prouctBean2);

        prouctBean2 = new ProductTypeBean();
        prouctBean2.prodcutTypeId = "2";
        prouctBean2.productTypeName = "车贷宝";
        prouctBean2.loanTypeId = 1;
        prouctBeanList.add(prouctBean2);


        prouctBean2 = new ProductTypeBean();
        prouctBean2.prodcutTypeId = "2";
        prouctBean2.productTypeName = "车贷宝";
        prouctBean2.loanTypeId = 1;
        prouctBeanList.add(prouctBean2);

        baseResultBean.data = prouctBeanList;

        return baseResultBean;
    }

    public static BaseResultBean<ProductProjectInfoBean> getSussefulResult27() {
        BaseResultBean<ProductProjectInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        ProductProjectInfoBean userBean = new ProductProjectInfoBean();
        userBean.url = "http://www.qq.com/";
        baseResultBean.data = userBean;
        Gson gson = new Gson();
        return baseResultBean;
    }

    public static BaseResultBean<RechargeInfoBean> getSussefulResult28() {
        BaseResultBean<RechargeInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        RechargeInfoBean userBean = new RechargeInfoBean();
        userBean.bankName = "招商银行";
        userBean.bankNum = "尾号8888*********";
        userBean.imageUrl = "http://d.hiphotos.baidu.com/zhidao/pic/item/d043ad4bd11373f0e90edb26a60f4bfbfbed0451.jpg";
        userBean.accountName = "李先生";
        userBean.rechargeState = RechargeStateEnum.NOT_FRIST_RECHAGRE.getValue();
        baseResultBean.data = userBean;
        Gson gson = new Gson();
        return baseResultBean;
    }

    public static BaseResultBean<RechargeResultInfoBean> getSussefulResult29() {
        BaseResultBean<RechargeResultInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        RechargeResultInfoBean userBean = new RechargeResultInfoBean();
        userBean.payParams = "招行 *****8888";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<WelcomeInfoBean> getSussefulResult30() {
        BaseResultBean<WelcomeInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        WelcomeInfoBean userBean = new WelcomeInfoBean();
        userBean.clickurl = "https://www.baidu.com";
        userBean.img = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1460700593&di=e2d7017c6885bc082c42fc58730b8e94&src=http://img.zcool.cn/community/01e15c55f9384c6ac7251df808bb29.jpg";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<List<TradeRecordBean>> getSussefulResult31() {
        BaseResultBean<List<TradeRecordBean>> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        List<TradeRecordBean> tradeRecordBeanList = new ArrayList<>();
        TradeRecordBean userBean = new TradeRecordBean();
        userBean.isAdding = MoneyIsAddEnum.IS_ADD_MOENY.getValue();
        userBean.tradeDate = "2015-01-021 12: 12:12";
        userBean.tradeMoney = "+200元";
        userBean.tradeName = "财富中国2015121212121";
        userBean.tradeTypeName = "充值";
        tradeRecordBeanList.add(userBean);


        userBean = new TradeRecordBean();
        userBean.isAdding = MoneyIsAddEnum.NOT_ADD_MOENY.getValue();
        userBean.tradeDate = "2025-01-01 12:12:12";
        userBean.tradeMoney = "-200元";
        userBean.tradeName = "财富中国2015大幅度2121";
        userBean.tradeTypeName = "充值";
        tradeRecordBeanList.add(userBean);


        userBean = new TradeRecordBean();
        userBean.isAdding = MoneyIsAddEnum.IS_ADD_MOENY.getValue();
        userBean.tradeDate = "2015-01-021 12: 12:12";
        userBean.tradeMoney = "+200元";
        userBean.tradeName = "财富中国2015121212121";
        userBean.tradeTypeName = "充值";
        tradeRecordBeanList.add(userBean);


        userBean = new TradeRecordBean();
        userBean.isAdding = MoneyIsAddEnum.IS_ADD_MOENY.getValue();
        userBean.tradeDate = "2015-01-021 12: 12:12";
        userBean.tradeMoney = "+200元";
        userBean.tradeName = "财富中国2015121212121";
        userBean.tradeTypeName = "充值";
        tradeRecordBeanList.add(userBean);


        userBean = new TradeRecordBean();
        userBean.isAdding = MoneyIsAddEnum.NOT_ADD_MOENY.getValue();
        userBean.tradeDate = "2015-01-021 12: 12:12";
        userBean.tradeMoney = "-5000元";
        userBean.tradeName = "财富中国2015121212121";
        userBean.tradeTypeName = "充值";
        tradeRecordBeanList.add(userBean);


        userBean = new TradeRecordBean();
        userBean.isAdding = MoneyIsAddEnum.IS_ADD_MOENY.getValue();
        userBean.tradeDate = "2015-01-021 12: 12:12";
        userBean.tradeMoney = "+200元";
        userBean.tradeName = "财富中国2015121212121";
        userBean.tradeTypeName = "充值";
        tradeRecordBeanList.add(userBean);


        userBean = new TradeRecordBean();
        userBean.isAdding = MoneyIsAddEnum.NOT_ADD_MOENY.getValue();
        userBean.tradeDate = "2015-11-21 12: 12:12";
        userBean.tradeMoney = "-200元";
        userBean.tradeName = "财富中国2015121212121";
        userBean.tradeTypeName = "提现";
        tradeRecordBeanList.add(userBean);

        baseResultBean.data = tradeRecordBeanList;
        return baseResultBean;
    }


    public static BaseResultBean<UpdateVersionBean> getSussefulResult32() {
        BaseResultBean<UpdateVersionBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        UpdateVersionBean userBean = new UpdateVersionBean();
        userBean.force_update = 0;
        userBean.url = "http://dd.myapp.com/16891/3C42979C2DE84802B4474F0BCB10D35E.apk?fsname=com.baidu.tieba_7.0.4_117441536.apk";
        userBean.version_code = 66;
        userBean.version_desc = "现在邀请您更新最新版本的2.0，全新UI全新体验，哈哈哈哈哈哈哈哈哈哈哈哈哈";
        baseResultBean.data = userBean;
        return baseResultBean;
    }


    public static BaseResultBean<VertifyInfoBean> getSussefulResult33() {
        BaseResultBean<VertifyInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";

        VertifyInfoBean vertifyInfo = new VertifyInfoBean();

        List<VertifyInfoBean.VertifyGroupInfo> vertifyGroupInfoList = new ArrayList<>();
        VertifyInfoBean.VertifyGroupInfo vertifyGroupInfo = vertifyInfo.new VertifyGroupInfo();
        vertifyGroupInfo.vertifyName = "基本身份";
        vertifyGroupInfo.vertifyResult = 1;
        vertifyGroupInfoList.add(vertifyGroupInfo);
        vertifyGroupInfo = vertifyInfo.new VertifyGroupInfo();
        vertifyGroupInfo.vertifyName = "基本身份";
        vertifyGroupInfo.vertifyResult = 1;
        vertifyGroupInfoList.add(vertifyGroupInfo);
        vertifyGroupInfo = vertifyInfo.new VertifyGroupInfo();
        vertifyGroupInfo.vertifyName = "身份证";
        vertifyGroupInfo.vertifyResult = 1;
        vertifyGroupInfoList.add(vertifyGroupInfo);
        vertifyGroupInfo = vertifyInfo.new VertifyGroupInfo();
        vertifyGroupInfo.vertifyName = "实况考察";
        vertifyGroupInfo.vertifyResult = -1;
        vertifyGroupInfoList.add(vertifyGroupInfo);
        vertifyGroupInfo = vertifyInfo.new VertifyGroupInfo();
        vertifyGroupInfo.vertifyName = "手机认证";
        vertifyGroupInfo.vertifyResult = 1;
        vertifyGroupInfoList.add(vertifyGroupInfo);
        vertifyGroupInfo = vertifyInfo.new VertifyGroupInfo();
        vertifyGroupInfo.vertifyName = "工作认真";
        vertifyGroupInfo.vertifyResult = 1;
        vertifyGroupInfoList.add(vertifyGroupInfo);
        vertifyGroupInfo = vertifyInfo.new VertifyGroupInfo();
        vertifyGroupInfo.vertifyName = "收入认证";
        vertifyGroupInfo.vertifyResult = 1;
        vertifyGroupInfoList.add(vertifyGroupInfo);
        vertifyInfo.vertifyResultlGroup = vertifyGroupInfoList;

        List<VertifyInfoBean.VertifyImageGroupInfo> vertifyImageGroupInfoList = new ArrayList<>();
        VertifyInfoBean.VertifyImageGroupInfo vertifyImageGroupInfo = vertifyInfo.new VertifyImageGroupInfo();
        vertifyImageGroupInfo.name = "身份证";
        vertifyImageGroupInfo.url = "http://img1.gtimg.com/news/pics/hv1/183/5/2042/132782508.jpg";
        vertifyImageGroupInfoList.add(vertifyImageGroupInfo);
        vertifyImageGroupInfo = vertifyInfo.new VertifyImageGroupInfo();
        vertifyImageGroupInfo.name = "工资证";
        vertifyImageGroupInfo.url = "http://img1.gtimg.com/news/pics/hv1/183/5/2042/132782508.jpg";
        vertifyImageGroupInfoList.add(vertifyImageGroupInfo);
        vertifyImageGroupInfo = vertifyInfo.new VertifyImageGroupInfo();
        vertifyImageGroupInfo.name = "工作证";
        vertifyImageGroupInfo.url = "http://img1.gtimg.com/news/pics/hv1/183/5/2042/132782508.jpg";
        vertifyImageGroupInfoList.add(vertifyImageGroupInfo);
        vertifyImageGroupInfo = vertifyInfo.new VertifyImageGroupInfo();
        vertifyImageGroupInfo.name = "东西证";
        vertifyImageGroupInfo.url = "http://img1.gtimg.com/news/pics/hv1/183/5/2042/132782508.jpg";
        vertifyImageGroupInfoList.add(vertifyImageGroupInfo);
        vertifyImageGroupInfo = vertifyInfo.new VertifyImageGroupInfo();
        vertifyImageGroupInfo.name = "这个证";
        vertifyImageGroupInfo.url = "http://img1.gtimg.com/news/pics/hv1/183/5/2042/132782508.jpg";
        vertifyImageGroupInfoList.add(vertifyImageGroupInfo);

        vertifyInfo.relativeData = vertifyImageGroupInfoList;

        baseResultBean.data = vertifyInfo;
        return baseResultBean;
    }


    public static BaseResultBean<WithDrawInfoBean> getSussefulResult34() {
        BaseResultBean<WithDrawInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        WithDrawInfoBean userBean = new WithDrawInfoBean();
        userBean.bankName = "招商银行";
        userBean.bankCardNum = "尾号8888*********";
        userBean.imageUrl = "http://d.hiphotos.baidu.com/zhidao/pic/item/d043ad4bd11373f0e90edb26a60f4bfbfbed0451.jpg";
        userBean.showCanWithdrawAmount = "500.00";
        userBean.tips = "快速提现:工作日当日到账,单笔限额20万，手续费5元每笔";
        userBean.bankCardId = "ss";
        baseResultBean.data = userBean;
        return baseResultBean;
    }


    public static BaseResultBean<List<WithdrawRecordInfoBean>> getSussefulResult35() {
        BaseResultBean<List<WithdrawRecordInfoBean>> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";

        List<WithdrawRecordInfoBean> withdrawRecordInfoList = new ArrayList<>();

        WithdrawRecordInfoBean userBean = new WithdrawRecordInfoBean();
        userBean.tips = "提现到丁先生银行账户";
        userBean.withdrawDate = "2016-01-01 12:12:12";
        userBean.withDrawlMoeny = "-100.00元";
        userBean.withdrawState = "确认转账";
        withdrawRecordInfoList.add(userBean);

        userBean = new WithdrawRecordInfoBean();
        userBean.tips = "提现到丁先生银行账户";
        userBean.withdrawDate = "2016-01-01 12:12:12";
        userBean.withDrawlMoeny = "-100.00元";
        userBean.withdrawState = "确认转账";
        withdrawRecordInfoList.add(userBean);

        userBean = new WithdrawRecordInfoBean();
        userBean.tips = "提现到丁先生银行账户";
        userBean.withdrawDate = "2016-01-01 12:12:12";
        userBean.withDrawlMoeny = "-100.00元";
        userBean.withdrawState = "确认转账";
        withdrawRecordInfoList.add(userBean);

        userBean = new WithdrawRecordInfoBean();
        userBean.tips = "提现到丁先生银行账户";
        userBean.withdrawDate = "2016-01-01 12:12:12";
        userBean.withDrawlMoeny = "-100.00元";
        userBean.withdrawState = "确认转账";
        withdrawRecordInfoList.add(userBean);

        userBean = new WithdrawRecordInfoBean();
        userBean.tips = "提现到丁先生银行账户";
        userBean.withdrawDate = "2016-01-01 12:12:12";
        userBean.withDrawlMoeny = "-100.00元";
        userBean.withdrawState = "确认转账";
        withdrawRecordInfoList.add(userBean);

        userBean = new WithdrawRecordInfoBean();
        userBean.tips = "提现到丁先生银行账户";
        userBean.withdrawDate = "2016-01-01 12:12:12";
        userBean.withDrawlMoeny = "-100.00元";
        userBean.withdrawState = "确认转账";
        withdrawRecordInfoList.add(userBean);

        userBean = new WithdrawRecordInfoBean();
        userBean.tips = "提现到丁先生银行账户";
        userBean.withdrawDate = "2016-01-01 12:12:12";
        userBean.withDrawlMoeny = "-100.00元";
        userBean.withdrawState = "确认转账";
        withdrawRecordInfoList.add(userBean);

        userBean = new WithdrawRecordInfoBean();
        userBean.tips = "提现到丁先生银行账户";
        userBean.withdrawDate = "2016-01-01 12:12:12";
        userBean.withDrawlMoeny = "-100.00元";
        userBean.withdrawState = "确认转账";
        withdrawRecordInfoList.add(userBean);

        userBean = new WithdrawRecordInfoBean();
        userBean.tips = "提现到丁先生银行账户";
        userBean.withdrawDate = "2016-01-01 12:12:12";
        userBean.withDrawlMoeny = "-100.00元";
        userBean.withdrawState = "确认转账";
        withdrawRecordInfoList.add(userBean);

        baseResultBean.data = withdrawRecordInfoList;
        return baseResultBean;
    }


    public static BaseResultBean<WithdrawRightInfoBean> getSussefulResult36() {
        BaseResultBean<WithdrawRightInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "登录成功";
        WithdrawRightInfoBean userBean = new WithdrawRightInfoBean();
        userBean.actualMoeny = "95.00元";
        userBean.withdrawMoney = "100.00元";
        userBean.bankStr = "招商 8888888******";
        baseResultBean.data = userBean;
        return baseResultBean;
    }

    public static BaseResultBean<VertifyCodeInfoBean> getSussefulResult37() {
        BaseResultBean<VertifyCodeInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "短信验证码获取成功成功";
        VertifyCodeInfoBean vertifyCodeInfo = new VertifyCodeInfoBean();
        vertifyCodeInfo.millisecond = 10000;

        baseResultBean.data = vertifyCodeInfo;

        Gson gson = new Gson();

        return baseResultBean;
    }


    public static BaseResultBean<MyLoanInfoBean> getSussefulResult38() {

        BaseResultBean<MyLoanInfoBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "短信验证码获取成功成功";
        MyLoanInfoBean vertifyCodeInfo = new MyLoanInfoBean();
        vertifyCodeInfo.bindingDays = "10天";
        vertifyCodeInfo.loanAddr = "广东省深圳市";
        vertifyCodeInfo.loanDateType = "按天借款";
        vertifyCodeInfo.loanDescrib = "系欸请安啊大苏打";
        vertifyCodeInfo.loanMoeny = "2亿";
        vertifyCodeInfo.loanTime = "30天";
        vertifyCodeInfo.loanTitle = "买车借款";
        vertifyCodeInfo.returnWay = "按天还款";
        vertifyCodeInfo.yearOfRate = "10%";
        baseResultBean.data = vertifyCodeInfo;
        Gson gson = new Gson();

        return baseResultBean;
    }


    public static BaseResultBean<List<MyLoanListBean>> getSussefulResult39() {

        BaseResultBean<List<MyLoanListBean>> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "短信验证码获取成功成功";
        List<MyLoanListBean> myLoanListList = new ArrayList<>();

        MyLoanListBean myLoanList = new MyLoanListBean();
        myLoanList.loanDate = "2015-01-01 12:12:12";
        myLoanList.loanId = "dd";
        myLoanList.loanMoeny = "300万";
        myLoanList.loanSate = "审核中";
        myLoanList.loanTitle = "生日借款";
        myLoanList.txtColor = "E84C3D";
        myLoanListList.add(myLoanList);

        myLoanList = new MyLoanListBean();
        myLoanList.loanDate = "2015-01-01 12:12:12";
        myLoanList.loanId = "dd";
        myLoanList.loanMoeny = "300万";
        myLoanList.loanSate = "成功借款";
        myLoanList.loanTitle = "生日借款";
        myLoanList.txtColor = "E84C3D";
        myLoanListList.add(myLoanList);

        myLoanList = new MyLoanListBean();
        myLoanList.loanDate = "2015-01-01 12:12:12";
        myLoanList.loanId = "dd";
        myLoanList.loanMoeny = "38万";
        myLoanList.loanSate = "审核中";
        myLoanList.loanTitle = "生日借款";
        myLoanList.txtColor = "E84C3D";
        myLoanListList.add(myLoanList);

        myLoanList = new MyLoanListBean();
        myLoanList.loanDate = "2015-01-01 12:12:12";
        myLoanList.loanId = "dd";
        myLoanList.loanMoeny = "300万";
        myLoanList.loanSate = "审核中";
        myLoanList.loanTitle = "生日xxxx借款";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);

        myLoanList = new MyLoanListBean();
        myLoanList.loanDate = "2015-01-01 12:12:12";
        myLoanList.loanId = "dd";
        myLoanList.loanMoeny = "300万";
        myLoanList.loanSate = "审核中";
        myLoanList.loanTitle = "生日借款";
        myLoanList.txtColor = "E84C3D";
        myLoanListList.add(myLoanList);


        myLoanList = new MyLoanListBean();
        myLoanList.loanDate = "2015-01-01 12:12:12";
        myLoanList.loanId = "dd";
        myLoanList.loanMoeny = "300万";
        myLoanList.loanSate = "审核中";
        myLoanList.loanTitle = "生日借款";
        myLoanList.txtColor = "E84C3D";
        myLoanListList.add(myLoanList);

        myLoanList = new MyLoanListBean();
        myLoanList.loanDate = "2015-01-01 12:12:12";
        myLoanList.loanId = "dd";
        myLoanList.loanMoeny = "300万";
        myLoanList.loanSate = "审核中";
        myLoanList.loanTitle = "生日借款";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);


        baseResultBean.data = myLoanListList;

        return baseResultBean;
    }


    public static BaseResultBean<List<MyGiftBean>> getSussefulResult40() {
        BaseResultBean<List<MyGiftBean>> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "短信验证码获取成功成功";
        List<MyGiftBean> myLoanListList = new ArrayList<>();


        MyGiftBean myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);

        myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);

        myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);


        myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);


        myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);


        myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);


        myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);


        myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);

        myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);

        myLoanList = new MyGiftBean();
        myLoanList.date = "2015-01-01";
        myLoanList.describe = "哈哈哈哈哈 啊啊啊啊啊啊啊啊啊啊啊哈哈哈哈啊哈";
        myLoanList.imgUrl = "http://images.cnitblog.com/i/446475/201407/191810473344044.jpg";
        myLoanList.state = "审核中";
        myLoanList.title = "生日借款啊啊啊啊啊啊";
        myLoanList.txtColor = "000000";
        myLoanListList.add(myLoanList);

        baseResultBean.data = myLoanListList;
        return baseResultBean;
    }

    public static BaseResultBean<List<MyLoanBean>> getSussefulResult45() {

        BaseResultBean<List<MyLoanBean>> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "获取信息成功";

        List<MyLoanBean> myLoanBeanList = new ArrayList<>();

        MyLoanBean myLoanBean = new MyLoanBean();
//        myLoanBean.loanTypeId = "1111";
//        myLoanBean.prodcutId = "2222";
//        myLoanBean.borrowName = "车辆抵押贷款";
//        myLoanBean.borrowMoney = "1000";
//        myLoanBean.borrowStat = "正在审核";
//        myLoanBean.repayModeId = "按月付息到期还本";
//        myLoanBean.bidDay = "5天";
//        myLoanBean.time = "2016-09-12";
//        myLoanBeanList.add(myLoanBean);
//
//        myLoanBean = new MyLoanBean();
//        myLoanBean.loanTypeId = "1111";
//        myLoanBean.prodcutId = "2222";
//        myLoanBean.borrowName = "车辆抵押贷款";
//        myLoanBean.borrowMoney = "1000";
//        myLoanBean.borrowStat = "正在审核";
//        myLoanBean.repayModeId = "按月付息到期还本";
//        myLoanBean.bidDay = "5天";
//        myLoanBean.time = "2016-09-12";
//        myLoanBeanList.add(myLoanBean);
//
//        myLoanBean = new MyLoanBean();
//        myLoanBean.loanTypeId = "1111";
//        myLoanBean.prodcutId = "2222";
//        myLoanBean.borrowName = "车辆抵押贷款";
//        myLoanBean.borrowMoney = "1000";
//        myLoanBean.borrowStat = "正在审核";
//        myLoanBean.repayModeId = "按月付息到期还本";
//        myLoanBean.bidDay = "5天";
//        myLoanBean.time = "2016-09-12";
//        myLoanBeanList.add(myLoanBean);
//
//        myLoanBean = new MyLoanBean();
//        myLoanBean.loanTypeId = "1111";
//        myLoanBean.prodcutId = "2222";
//        myLoanBean.borrowName = "车辆抵押贷款";
//        myLoanBean.borrowMoney = "1000";
//        myLoanBean.borrowStat = "正在审核";
//        myLoanBean.repayModeId = "按月付息到期还本";
//        myLoanBean.bidDay = "5天";
//        myLoanBean.time = "2016-09-12";
//        myLoanBeanList.add(myLoanBean);
//        myLoanBean = new MyLoanBean();
//        myLoanBean.loanTypeId = "1111";
//        myLoanBean.prodcutId = "2222";
//        myLoanBean.borrowName = "车辆抵押贷款";
//        myLoanBean.borrowMoney = "1000";
//        myLoanBean.borrowStat = "正在审核";
//        myLoanBean.repayModeId = "按月付息到期还本";
//        myLoanBean.bidDay = "5天";
//        myLoanBean.time = "2016-09-12";
//        myLoanBeanList.add(myLoanBean);
//        myLoanBean = new MyLoanBean();
//        myLoanBean.loanTypeId = "1111";
//        myLoanBean.prodcutId = "2222";
//        myLoanBean.borrowName = "车辆抵押贷款";
//        myLoanBean.borrowMoney = "1000";
//        myLoanBean.borrowStat = "正在审核";
//        myLoanBean.repayModeId = "按月付息到期还本";
//        myLoanBean.bidDay = "5天";
//        myLoanBean.time = "2016-09-12";
//        myLoanBeanList.add(myLoanBean);
//        myLoanBean = new MyLoanBean();
//        myLoanBean.loanTypeId = "1111";
//        myLoanBean.prodcutId = "2222";
//        myLoanBean.borrowName = "车辆抵押贷款";
//        myLoanBean.borrowMoney = "1000";
//        myLoanBean.borrowStat = "正在审核";
//        myLoanBean.repayModeId = "按月付息到期还本";
//        myLoanBean.bidDay = "5天";
//        myLoanBean.time = "2016-09-12";
//        myLoanBeanList.add(myLoanBean);
//        myLoanBean = new MyLoanBean();
//        myLoanBean.loanTypeId = "1111";
//        myLoanBean.prodcutId = "2222";
//        myLoanBean.borrowName = "车辆抵押贷款";
//        myLoanBean.borrowMoney = "1000";
//        myLoanBean.borrowStat = "正在审核";
//        myLoanBean.repayModeId = "按月付息到期还本";
//        myLoanBean.bidDay = "5天";
//        myLoanBean.time = "2016-09-12";
//        myLoanBeanList.add(myLoanBean);
//        myLoanBean = new MyLoanBean();
//        myLoanBean.loanTypeId = "1111";
//        myLoanBean.prodcutId = "2222";
//        myLoanBean.borrowName = "车辆抵押贷款";
//        myLoanBean.borrowMoney = "1000";
//        myLoanBean.borrowStat = "正在审核";
//        myLoanBean.repayModeId = "按月付息到期还本";
//        myLoanBean.bidDay = "5天";
//        myLoanBean.time = "2016-09-12";
        myLoanBeanList.add(myLoanBean);

        baseResultBean.data = myLoanBeanList;

        return baseResultBean;
    }

    public static BaseResultBean<MyLoanDetailsBean> getSussefulResult46() {
        BaseResultBean<MyLoanDetailsBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "获取信息成功";

        MyLoanDetailsBean myLoanDetailsBean = new MyLoanDetailsBean();
        List<MyLoanDetailsBean.ImageInfo> imageInfos = new ArrayList<>();
        MyLoanDetailsBean.ImageInfo imageInfo = null;
        for (int i = 0; i < 10; i++) {
            imageInfo = myLoanDetailsBean.new ImageInfo();
            imageInfo.name = "身份证";
            imageInfo.url = "http://img2.3lian.com/2014/f5/135/d/92.jpg";
            imageInfos.add(imageInfo);
        }
        myLoanDetailsBean.loanTime = "2107-78-87";
        myLoanDetailsBean.loanDescrib = "啊但是佛啊说法懂啊是能够啊弄啊撒旦发送是的佛大法师打发的高发";
        myLoanDetailsBean.loanDate = "6个月";
        myLoanDetailsBean.loanMoeny = "50000";
//        myLoanDetailsBean.loanStats = 1;
        myLoanDetailsBean.loanSateText = "筹款成功";
        myLoanDetailsBean.returnWay = "按月付息到期还本";
        myLoanDetailsBean.productName = "车辆抵押贷款";
        myLoanDetailsBean.bindingDays = "10天";
        myLoanDetailsBean.relativeData = imageInfos;

        baseResultBean.data = myLoanDetailsBean;
        return baseResultBean;
    }


    public static BaseResultBean<AuditStateBean> getSussefulResult48() {
        BaseResultBean<AuditStateBean> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "获取信息成功";

        AuditStateBean auditStateBean = new AuditStateBean();
        auditStateBean.failureDescribe = "地发呆佛啊的念佛爱的你搞id功能撒旦发个都昂法规懊恼奋斗啊是哪嘎";
        auditStateBean.loanSateText = "筹款成功";
        AuditStateBean.AuditInfo auditInfo = null;
        List<AuditStateBean.AuditInfo> list = new ArrayList<>();
        for (int i = 0;i<5;i++){
            auditInfo = auditStateBean.new AuditInfo();
            auditInfo.productName = "车辆抵押贷款";
            auditInfo.yearOfRate = "12.68%";
            auditInfo.investDate = "6";
            auditInfo.remiansMoeny = "10000";
            auditInfo.loanTypeId = "1";
            auditInfo.prodcutId = "2";
            auditInfo.unit = 3;
            auditInfo.btntxt = "查看详情";
            auditInfo.canInvest = -1;

            list.add(auditInfo);
        }
//        auditStateBean.products = list;

        baseResultBean.data = auditStateBean;
        return baseResultBean;
    }


    public static BaseResultBean<LoanPersonInfo> getSussefulResult49() {
        BaseResultBean<LoanPersonInfo> baseResultBean = new BaseResultBean<>();
        baseResultBean.code = APIService.OK_CODE;
        baseResultBean.msg = "获取信息成功";

        LoanPersonInfo loanPersonInfo = new LoanPersonInfo();
        List<LoanPersonInfo.PersonInfo> list = new ArrayList<>();
        LoanPersonInfo.PersonInfo personInfo = null;
        for (int i = 0; i < 5; i++) {
            personInfo = loanPersonInfo.new PersonInfo();
            personInfo.loanerInfo = "职业身份";
            personInfo.info = "企业主";
            list.add(personInfo);
        }

        loanPersonInfo.loanInfos = list;
        baseResultBean.data = loanPersonInfo;
        return baseResultBean;
    }
}
