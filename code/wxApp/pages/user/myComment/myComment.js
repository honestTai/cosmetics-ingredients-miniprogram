// pages/user/myForum/myForum.js
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
      commentList:[]
    },

    view(e){
      let type = e.currentTarget.dataset.type
      if(type === 1){
        wx.navigateTo({
          url: '../../../pages/detail/comment?id='+e.currentTarget.dataset.gid,
        })
      }else{
        wx.navigateTo({
          url: '../../../pages/comment/list/index?goodsId='+e.currentTarget.dataset.gid,
        })
      }
    },

    del(e){
      let that = this
      wx.showModal({
        title: '警告',
        content: '确定删除吗',
        cancelText: '不确定',
        confirmText: '确定',
        success: res => {
          if (res.confirm) {
            app.api.delComment(e.currentTarget.dataset.id)
            .then(res => {
              that.getCommentList()
            })
          }
        }
      })
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
      this.getCommentList()
    },

    getCommentList(){
      app.api.myCommentGet()
      .then(res => {
        let commentList = res.data
        this.setData({
          commentList: commentList
        })
      })
    },

      // ListTouch触摸开始
  ListTouchStart(e) {
    this.setData({
      ListTouchStart: e.touches[0].pageX
    })
  },

  // ListTouch计算方向
  ListTouchMove(e) {
    this.setData({
      ListTouchDirection: e.touches[0].pageX - this.data.ListTouchStart > 0 ? 'right' : 'left'
    })
  },

  // ListTouch计算滚动
  ListTouchEnd(e) {
    if (this.data.ListTouchDirection =='left'){
      this.setData({
        modalName: e.currentTarget.dataset.target
      })
    } else {
      this.setData({
        modalName: null
      })
    }
    this.setData({
      ListTouchDirection: null
    })
  },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})