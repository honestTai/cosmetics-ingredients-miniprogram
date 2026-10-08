// pages/fourm/fourm/comment.js
const app=getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
      forum:{},
      isCard:true
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
      this.getForumData(options.id)
    },
    onLoad(options) {
      this.getForumData(options.id)
    },
    viewPic(e) {
      let picList = e.currentTarget.dataset.pic
      wx.previewImage({
        urls: picList,
        current: picList[0]
      });
    },
    addComment(e){
      console.log(e)
      wx.navigateTo({
        url: '../../pages/addComment/add?id='+e.currentTarget.dataset.id+'&commentid='+e.currentTarget.dataset.commentid,
      })
    },
    getForumData(id){
      app.api.forumGet(id)
      .then(res => {
        let forum = res.data
        this.setData({
          forum: forum
        })
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