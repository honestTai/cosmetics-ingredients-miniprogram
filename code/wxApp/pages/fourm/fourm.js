const app = getApp()
Page({
  data: {
    config: app.globalData.config,
    page: {
      searchCount: false,
      current: 1,
      size: 10,
      likeSearch:""
    },
    loadmore: true,
    fourmList: [],
    fourmListNew: [],
    fourmListHot: [],
    cardCur: 0,
    noticeData: [],
    isCard:true
  },
  onShow() {
    app.initPage()
      .then(res => {
        this.setData({
          loadmore: true,
          ['page.current']: 1,
          fourmList: [],
          fourmListNew: [],
          fourmListHot: []
        })
        this.loadData()
      })
  },
  loadData(){
    this.forumPage()
  },
  viewPic(e) {
    console.log(e)
    let picList = e.currentTarget.dataset.pic
    wx.previewImage({
      urls: picList,
      current: picList[0]
    });
  },
  searchHandle(e) {
    let likeSearchValue = e.detail.value; // 获取搜索框输入的值
    this.setData({
      loadmore: true,
      ['page.current']: 1,
      "page.likeSearch": likeSearchValue, // 更新 likeSearch 属性
      fourmList: [],
      fourmListNew: [],
      fourmListHot: []
    })
    this.forumPage()
  },
  addFourm(e){
    console.log(e)
    //跳转动态发布页面
    wx.navigateBack({
      url: '../../pages/fourm/fourm/add.wxml'
    })
  },
  forumPage(e) {
    app.api.forumPage(this.data.page)
      .then(res => {
        let fourmList = res.data.records
        this.setData({
          fourmList: [...this.data.fourmList, ...fourmList]
        })
        if (fourmList.length < this.data.page.size) {
          this.setData({
            loadmore: false
          })
        }
      })
  },
  refresh(){
    this.setData({
      loadmore: true,
      ['page.current']: 1,
      fourmList: [],
      fourmListNew: [],
      fourmListHot: []
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
