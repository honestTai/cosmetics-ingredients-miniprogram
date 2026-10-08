// pages/fourm/fourm/comment/add.js
const app=getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
      textareaAValue:'',
      id:null,
      commentId:null
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
      this.setData({
        id:options.id,
        commentId:options.commentid
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
    textareaAInput(e) {
      this.setData({
        textareaAValue: e.detail.value
      })
    },
    send(e){
      var that=this
      app.api.comment({
        goodsId: that.data.id,
        content: that.data.textareaAValue,
        id:that.data.commentId
      }, (res) => {
       
      })
      setTimeout(function() {
        wx.navigateTo({
          url: '../../pages/detail/comment?id='+that.data.id,
        }) 
      }, 1200);
         
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