
const app = getApp()
import __config from '../../config/env'
Page({
  data: {
    config: app.globalData.config,
    page: {
      searchCount: false,
      current: 1,
      size: 10
    },
    loadmore: true,
    goodsList: [],
    goodsListNew: [],
    goodsListHot: [],
    swiperData: [
      __config.picPath+'/1.jpg',
      __config.picPath+'/2.jpg'
    ],
    cardCur: 0,
    noticeData: [],
    goodsListByScore:[]
  },
  onLoad() {
    app.initPage()
      .then(res => {
        this.loadData()
      })
  },
  onShow(){
   
  },
  loadData(){
    this.goodsPage()
    this.goodsScorePage()
  },
  goodsScorePage(){
    app.api.goodsScorePage(this.data.page)
    .then(res => {
      let goodsListByScore = res.data.records
      this.setData({
        goodsListByScore: [...this.data.goodsListByScore, ...goodsListByScore]
      })
      if (goodsListByScore.length < this.data.page.size) {
        this.setData({
          loadmore: false
        })
      }
    })
  },
  toHonorList(){
    wx.navigateTo({
      url: '../../pages/honor/honor',
    })
  },
  onShareAppMessage: function () {
    let title = '靓靓化妆品小程序'
    let path = 'pages/home/index'
    return {
      title: title,
      path: path,
      success: function (res) {
        if (res.errMsg == 'shareAppMessage:ok') {
          console.log(res.errMsg)
        }
      },
      fail: function (res) {
        // 转发失败
      }
    }
  },
  goodsPage(e) {
    app.api.goodsPage(this.data.page)
      .then(res => {
        let goodsList = res.data.records
        this.setData({
          goodsList: [...this.data.goodsList, ...goodsList]
        })
        if (goodsList.length < this.data.page.size) {
          this.setData({
            loadmore: false
          })
        }
      })
  },
  refresh(){
    this.setData({
      loadmore: true,
      ['page.current']: this.data.page.current + 1,
      // goodsList: [],
      goodsListNew: [],
      goodsListHot: []
    })
    this.loadData()
  },
  onPullDownRefresh(){
    // 显示顶部刷新图标
    wx.showNavigationBarLoading()
    this.refresh()
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading()
    // 停止下拉动作
    wx.stopPullDownRefresh()
  },
  onReachBottom() {
    if (this.data.loadmore) {
      this.setData({
        ['page.current']: this.data.page.current + 1
      })
      this.goodsPage()
    }
  },
  jumpPage(e){
    let page = e.currentTarget.dataset.page
    if (page){
      wx.navigateTo({
        url: page
      })
    }
  }
})
