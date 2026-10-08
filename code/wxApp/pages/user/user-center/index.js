
const app = getApp()

Page({
  data: {
    config: app.globalData.config,
    wxUser: null,
    userInfo: null,
    orderCountAll: []
  },
  onShow(){
   
    
    let wxUser = app.globalData.wxUser
    this.setData({
      wxUser: wxUser
    })
    this.wxUserGet()
  },
  onLoad(){
  },
  /**
   * 小程序设置
  */
  settings: function () {
    wx.openSetting({
      success: function (res) {
        console.log(res.authSetting)
      }
    })
  },
  agreeGetUser(e) {
    if (e.detail.errMsg == 'getUserInfo:ok') {
      app.api.wxUserSave(e.detail.userInfo)
        .then(res => {
          let wxUser = res.data
          this.setData({
            wxUser: wxUser
          })
          app.globalData.wxUser = wxUser
          this.wxUserGet()
        })
    }
  },
  //获取商城用户信息
  wxUserGet(){
    app.api.wxUserGet()
      .then(res => {
        this.setData({
          userInfo: res.data
        })
      })
  },
  orderCountAll(){
    app.api.orderCountAll()
      .then(res => {
        this.setData({
          orderCountAll: res.data
        })
      })
  }
})
