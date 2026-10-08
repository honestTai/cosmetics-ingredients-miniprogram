<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-calendar"></i> 榜单管理</el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 榜单列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>


        <div class="container">
            <div class="handle-box">
                <el-input v-model="query.likeName" placeholder="请输入榜单名称" class="handle-input mr10"
                          @change="searchUserName" v-if="activeName == 0"></el-input>

                <el-select v-model="query.goodsId" placeholder="产品选择" filterable clearable v-else @change="searchGoods">
                    <el-option v-for="item in goods" :value="item.id"
                               :key="item.id" :label="item.name">
                    </el-option>
                </el-select>
            </div>
            <div class="handle-box">
                <el-button
                    type="primary"
                    icon="el-icon-lx-add"
                    class="handle-del mr10"
                    @click="addHonor()"
                >添加榜单
                </el-button>
            </div>
            <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
                <el-tab-pane label="榜单列表">
                    <el-table
                        :data="listHonor"
                        style="width: 100%;margin-bottom: 20px;"
                        row-key="id"
                        border
                        default-expand-all>
                        <el-table-column
                            prop="id"
                            label="序号"
                            width="180">
                        </el-table-column>
                        <el-table-column
                            prop="name"
                            label="名称"
                        >
                        </el-table-column>
                        <el-table-column label="操作" width="180" align="center" fixed="right">
                            <template slot-scope="scope">
                                <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="honorOperation(scope.$index, scope.row,2,6,true)"
                                >榜单删除
                                </el-button>
                                <el-button
                                    type="text"
                                    icon="el-icon-edit"
                                    class="green"
                                    @click="addHonorGoods(scope.row.id,true)"
                                >添加产品
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane label="榜单商品">
                    <el-table
                        :data="listHonor"
                        style="width: 100%;margin-bottom: 20px;"
                        row-key="id"
                        border
                        default-expand-all>
                        <el-table-column
                            prop="goods.name"
                            label="产品名称"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="aclass.name"
                            label="所属分类"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="honor.name"
                            label="所属榜单"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="orders"
                            label="排序"
                        >
                        </el-table-column>
                        <el-table-column
                            prop="reason"
                            label="上榜理由"
                        >
                        </el-table-column>
                        <el-table-column label="操作" width="180" align="center" fixed="right">
                            <template slot-scope="scope">
                                <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="honorOperation(scope.$index, scope.row,2,7,false)"
                                >产品删除
                                </el-button>
                                <el-button
                                    type="text"
                                    icon="el-icon-edit"
                                    class="green"
                                    @click="addHonorGoods(scope.row,false)"
                                >修改
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
            <el-dialog title="添加榜单产品" :visible.sync="addHonorGoodsModel" width="50%">
                <el-form ref="form" :model="honorGoods" label-width="70px">
                    <el-form-item label="产品选择">
                        <el-select v-model="honorGoods.goodsId" placeholder="产品选择" filterable clearable>
                            <el-option v-for="item in goods" :value="item.id"
                                       :key="item.id" :label="item.name">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="排序">
                        <el-input-number v-model="honorGoods.orders" :step="1" step-strictly></el-input-number>
                    </el-form-item>
                    <el-form-item label="理由">
                        <el-input v-model="honorGoods.reason"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="addHonorGoodsModel = false">关闭</el-button>
                    <el-button type="primary" @click="submitaddHonorGoods">保存</el-button>
                </span>
            </el-dialog>

            <!--添加榜单弹窗-->
            <el-dialog title="添加榜单" :visible.sync="addHonorModel" width="50%">
                <el-form ref="form" :model="honor" label-width="70px">
                    <el-form-item label="榜单名称">
                        <el-input v-model="honor.name"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="addHonorModel = false">关闭</el-button>
                    <el-button type="primary" @click="submitaddHonor">保存</el-button>
                </span>
            </el-dialog>


        </div>
    </div>
</template>


<script>
import { baseUrl, getAllGoods, list, parentClass, userRegister } from '../../../utils';
import operateJs from '@/components/util/operateJS';
import axios from 'axios';

export default {
    data() {
        return {
            honor: {
                name: ''
            },
            honorGoods: {
                goodsId: '',
                reason: '',
                orders: 1,
                honorId: ''
            },
            query: {
                page: 1,
                pageSize: 10,
                classId: '',
                listType: 6,
                likeName: '',
                userType: 0,
                phone: '',
                goodsId: ''
            },
            pageTotal: 0,
            listHonor: [],
            operationVto: {
                honor: {},
                //0禁止登录1重置密码2允许登录3修改信息4添加榜单
                operationType: null,
                type: 1,
                honorGoods: {}
            },
            //默认展示的tag
            activeName: 0,
            userType: '',
            loginUserId: '',
            meStatus: '',
            showAdd: false,
            addHonorModel: false,
            addHonorGoodsModel: false,
            goods: [],
            addOrUpdate:false
        };
    },
    created() {
        this.getlistHonor();
        let user = JSON.parse(localStorage.getItem('userInfo'));
        this.userType = user.type;
        this.loginUserId = user.id;
        this.getAllGoods();
    },
    methods: {
        getAllGoods() {
            getAllGoods().then(res => {
                this.goods = res.data.data;
            });
        },
        clear() {
            this.honor = {
                name: ''
            };
            this.honor = {
                goodsId: '',
                reason: '',
                orders: 1,
                honorId: ''
            }
        },
        addHonor() {
            this.clear();
            this.addHonorModel = true;
        },
        honorOperation(index, row, operationType, type,honorBoolean) {
            var that = this;
            that.operationVto.operationType = operationType;
            this.operationVto.type = type;
            if(honorBoolean){
                that.operationVto.honor = row;
            }
            if(!honorBoolean){
                that.operationVto.honorGoods = row;
            }
            operateJs.systemOperation(this.operationVto);
            /**
             * 等到2秒刷新
             */
            setTimeout(function() {
                that.getlistHonor();
            }, '2000');
        },
        handleClick(tab, event) {
            let index = tab.index;
            console.log(index);
            if (index == 0) {
                this.query.listType = 6;
            } else {
                this.query.listType = 7;
            }
            this.getlistHonor();
        },
        getlistHonor() {
            list(this.query).then(res => {
                let data = res.data;
                this.pageTotal = data.data.total;
                this.listHonor = data.data.records;
            });
        },
        //分类查找
        searchUserName(val) {
            this.$set(this.query, 'likeName', val);
            this.getlistHonor();
        },
        searchGoods(val) {
            this.$set(this.query, 'goodsId', val);
            this.getlistHonor();
        },

        // //分页跳转
        handlePageChange(val) {
            this.$set(this.query, 'page', val);
            this.getlistHonor();
        },
        //每页大小改变
        handlePageSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getlistHonor();
        },
        //发送添加榜单请求
        submitaddHonor(e) {
            let that = this;
            let param = {
                honor: { ...that.honor },
                operationType: 1,
                type: 6
            };
            operateJs.systemOperation(param);
            this.addHonorModel = false;
            /**
             * 等到2秒刷新
             */
            setTimeout(function() {
                that.getlistHonor();
            }, '2000');
        },
        submitaddHonorGoods() {
            let that = this;
            let param ={}
            if(that.addOrUpdate){
                 param = {
                    honorGoods: {
                        goodsId: that.honorGoods.goodsId,
                        reason: that.honorGoods.reason,
                        orders: that.honorGoods.orders,
                        honorId: that.honorGoods.honorId
                    },
                    operationType: 1,
                    type: 7
                };
            }else {
                 param = {
                    honorGoods: {
                        goodsId: that.honorGoods.goodsId,
                        reason: that.honorGoods.reason,
                        orders: that.honorGoods.orders
                    },
                    operationType: 0,
                    type: 7
                };
            }

            operateJs.systemOperation(param);
            this.addHonorGoodsModel = false;
            /**
             * 等到2秒刷新
             */
            setTimeout(function() {
                that.getlistHonor();
            }, '2000');
        },
        addHonorGoods(e,a) {
            this.clear();
            this.addOrUpdate = a
            this.honorGoods = {...e}
            this.honorGoods.honorId = e;
            this.addHonorGoodsModel = true;
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
