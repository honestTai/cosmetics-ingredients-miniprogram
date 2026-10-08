
const app = getApp()

Page({
  data: {
    isCard:true,
    page: {
      current: 1,
      size: 10,
      goodsId:''
    },
    parameter: {
      
    },
    loadmore: true,
    goodsAppraises: []
  },
  onLoad(options) {
    let spuId = options.goodsId
    console.log(spuId)
    this.setData({
      ['parameter.spuId']: spuId,
      ['page.goodsId']: spuId
    })
    app.initPage()
      .then(res => {
        this.goodsCommentPage()
      })
  },
  goodsCommentPage() {
    app.api.goodsCommentPage(Object.assign(
      {},
      this.data.page
    ))
      .then(res => {
        let goodsAppraises = res.data.records
        this.setData({
          goodsAppraises: [...this.data.goodsAppraises, ...goodsAppraises]
        })
        if (goodsAppraises.length < this.data.page.size) {
          this.setData({
            loadmore: false
          })
        }
      })
  },
  onReachBottom() {
    if (this.data.loadmore) {
      this.setData({
        ['page.current']: this.data.page.current + 1
      })
      this.goodsCommentPage()
    }
  },
  refresh(){
    this.setData({
      loadmore: true,
      ['page.current']: this.data.page.current + 1,
    })
    this.goodsCommentPage()
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
  previewImage(e){
    wx.previewImage({
      urls: e.currentTarget.dataset.url,
      current: e.currentTarget.dataset.url
    });
  }
})
