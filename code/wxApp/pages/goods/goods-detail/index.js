const WxParse = require('../../../public/wxParse/wxParse.js')
const {
  base64src
} = require('../../../utils/base64src.js')
const app = getApp()

Page({
  data: {
    config: app.globalData.config,
    goodsSpu: null,
    currents: 1,
    cartNum: 1,
    goodsSpecData: [],
    shareShow: '',
    modalService: '',
    shoppingCartId: '',
    modalName: null,
    goodsList: [],
    id: 0
  },
  hideModal(e) {
    this.setData({
      modalName: null
    })
  },
  onLoad(options) {
    let id
    if (options.scene) { //接受二维码中参数
      id = decodeURIComponent(options.scene)
    } else {
      id = options.id
    }
    this.setData({
      id: id
    })
    app.initPage()
      .then(res => {
        this.goodsGet(id)

      })

  },
  onShareAppMessage: function () {
    let goodsSpu = this.data.goodsSpu
    let title = goodsSpu.goods.name
    let imageUrl = goodsSpu.goods.goodsPhotoList[0]
    let path = 'pages/goods/goods-detail/index?id=' + goodsSpu.goods.id
    return {
      title: title,
      path: path,
      imageUrl: imageUrl,
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
  goodsGet(id) {
    app.api.goodsGet(id)
      .then(res => {
        let goodsSpu = res.data
        this.setData({
          goodsSpu: goodsSpu
        })
        //html转wxml
        WxParse.wxParse('description', 'html', goodsSpu.goods.introduce, this, 0)
      })
  },

  change: function (e) {
    this.setData({
      currents: e.detail.current + 1
    })
  },


  operateCartEvent() {
    this.shoppingCartCount()
  },
  shareShow() {
    this.setData({
      shareShow: 'show'
    })
  },
  toComment(e) {
    console.log(e)
    wx.navigateTo({
      url: '../../../pages/goodsComment/add?id=' + e.currentTarget.dataset.id,
    })
  },
  checkProduct(e) {
    app.api.getAllGoods(e.currentTarget.dataset.id)
      .then(res => {
        let goodsList = res.data
        this.setData({
          goodsList: goodsList,
          modalName: "RadioModal"
        })
      })
    // wx.navigateTo({
    //   url: '../../../pages/check/check?id=' + e.currentTarget.dataset.id,
    // })
  },
  checkProductByGoods(e) {
    this.hideModal()
    wx.navigateTo({
      url: '../../../pages/check/check?pid=' + e.currentTarget.dataset.id + '&id=' + this.data.id,
    })
  },
  shareHide() {
    this.setData({
      shareShow: ''
    })
  },
  onPosterSuccess(e) {
    const {
      detail
    } = e
    this.setData({
      posterUrl: detail
    })
  },
  onPosterFail(err) {
    console.error(err);
  },
  hidePosterShow() {
    this.setData({
      posterShow: false,
      shareShow: ''
    })
  },
  /**
   * 异步生成海报
   */
  onCreatePoster() {

  },
  //点击保存到相册
  savePoster: function () {

  },
  handleContact(e) {
    console.log(e)
  }
})