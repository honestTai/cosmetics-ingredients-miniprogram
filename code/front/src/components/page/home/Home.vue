<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-calendar"></i> 评论动态管理</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>


        <div class="container">
            <div class="handle-box">
                <el-input v-model="query.likeName" placeholder="请输入查询内容" class="handle-input mr10"
                          @change="searchInfo"></el-input>
            </div>
            <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
                <el-tab-pane label="动态数据" name="0">
                    <el-table
                        :data="listUser"
                        style="width: 100%;margin-bottom: 20px;"
                        row-key="id"
                        border
                        default-expand-all>
                        <el-table-column
                            prop="user.name"
                            label="发布用户"
                            width="180">
                        </el-table-column>
                        <!--图片-->
                        <el-table-column
                            label="图片"
                        >
                            <template slot-scope="scope">
                                <el-image
                                    v-if="scope.row.picList && scope.row.picList.length > 0"
                                    style="width: 100px; height: 100px"
                                    :src="scope.row.picList[0]"
                                    :preview-src-list="scope.row.picList"
                                ></el-image>
                                <span v-else>暂无图片</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                            prop="title"
                            label="标题"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="info"
                            label="内容"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="time"
                            label="发布时间"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="comment"
                            label="评论数"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="view"
                            label="查看人数"
                        >
                        </el-table-column>
                        <el-table-column label="操作" width="180" align="center" fixed="right">
                            <template slot-scope="scope">
                                <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="forumOperation(scope.$index, scope.row,4)"
                                >删除数据
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane label="评论数据" name="1">

                    <el-table
                        :data="listUser"
                        style="width: 100%;margin-bottom: 20px;"
                        row-key="id"
                        border
                        >
                        <el-table-column
                            prop="forum.info"
                            label="动态内容"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="evUser.name"
                            label="评论人"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="content"
                            label="评论内容"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="time"
                            label="评论时间"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="time"
                            label="回复人"
                        >
                            <template slot-scope="scope">
                                {{ scope.row.replyId != null ? scope.row.reUser.name : '暂无回复' }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            prop="time"
                            label="回复内容"
                        >
                            <template slot-scope="scope">
                                {{ scope.row.replyId != null ? scope.row.replyContent : '暂无回复' }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            prop="time"
                            label="回复时间"
                        >
                            <template slot-scope="scope">
                                {{ scope.row.replyId != null ? scope.row.replyTime : '暂无回复' }}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="180" align="center" fixed="right">
                            <template slot-scope="scope">
                                <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="forumOperation(scope.$index, scope.row,5)"
                                >删除数据
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
            </el-tabs>
            <div class="pagination">
                <el-pagination
                    background
                    layout="total,sizes, prev, pager, next, jumper"
                    @size-change="handlePageSizeChange"
                    @current-change="handlePageChange"
                    :current-page="query.page"
                    :page-sizes="[10, 50, 100, 200,500,1000]"
                    :page-size="query.pageSize"
                    :total="pageTotal"
                >
                </el-pagination>
            </div>

        </div>
    </div>
</template>


<script>
import { list } from '../../../utils';
import operateJs from '@/components/util/operateJS';
import axios from 'axios';

export default {
    data() {
        return {
            query: {
                page: 1,
                pageSize: 10,
                listType: 4,
                likeName: '',
                commentType: 0
            },
            pageTotal: 0,
            listUser: [],
            operationVto: {
                forum: {},
                //0禁止登录1重置密码2允许登录3修改信息4添加用户
                operationType: null,
                type: 1
            },
            //默认展示的tag
            activeName: '0',
            userType: '',
            loginUserId: '',
            meStatus: '',
            showAdd: false,
            addAdminUserModel: false
        };
    },
    created() {
        this.getListForum();
    },
    methods: {
        forumOperation(index, row, type) {
            var that = this;
            that.operationVto.type = type;
            that.operationVto.forum = row;
            that.operationVto.evaluation = row;
            that.operationVto.operationType = 2
            operateJs.systemOperation(this.operationVto);
            /**
             * 等到2秒刷新
             */
            setTimeout(function() {
                that.getListForum();
            }, '2000');
        },
        handleClick(tab) {
            let index = tab.index;
            if (index == 0) {
                this.query.listType = 4;
            } else {
                this.query.listType = 5;
                this.query.commentType = 1;
            }
            this.getListForum();
        },
        getListForum() {
            list(this.query).then(res => {
                let data = res.data;
                this.pageTotal = data.data.total;
                this.listUser = data.data.records;
            });
        },
        searchInfo(val) {
            this.$set(this.query, 'likeName', val);
            this.getListForum();
        },
        // //分页跳转
        handlePageChange(val) {
            this.$set(this.query, 'page', val);
            this.getListForum();
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getListForum();
        }
    }
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}

.table {
    width: 100%;
    font-size: 14px;
}

.red {
    color: #ff0000;
}

.mr10 {
    margin-right: 10px;
}

</style>
