<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.userName" placeholder="用户名"></el-input>
				</el-form-item>
				<el-form-item>
					<el-input v-model="filters.phoneNum" placeholder="手机号码"></el-input>
				</el-form-item>

				<el-form-item>
					<el-button type="primary" v-on:click="getUsers">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="contents" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>
			<el-table-column prop="userName" label="用户名" width="200">
			</el-table-column>
			<el-table-column prop="phoneNum" label="手机号码" width="200">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="200" :formatter="formatTime">
			</el-table-column>
			<el-table-column label="操作" width="200">
				<template scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
			<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
			</el-pagination>
		</el-col>

		<!--编辑界面-->
		<el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">

				<el-form-item label="用户名">
					<el-input type="input" v-model="editForm.userName" :maxlength="50"></el-input>
				</el-form-item>
				<el-form-item label="手机号码">
					<el-input type="input" v-model="editForm.phoneNum"  :minlength="11" :maxlength="11"></el-input>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
			</div>
		</el-dialog>

		<!--新增界面-->
		<el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
			<el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
				<el-form-item label="用户名">
					<el-input type="input" v-model="addForm.userName" :maxlength="50"></el-input>
				</el-form-item>
				<el-form-item label="手机号码">
					<el-input type="input" v-model="addForm.phoneNum"  :minlength="11" :maxlength="11"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="addFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
    import util from '../../common/js/util';
    import moment from 'moment'
    import NProgress from 'nprogress';
	import {
		addUser, delUser,
		editUser,
		listUser
	} from '../../api/api';

    export default {
		data:function() {
			return {
				filters: {
                    userName:null,
                    phoneNum: null
				},
                contents: [],
				total: 0,
				page: 1,
				listLoading: false,
				sels: [],//列表选中列

				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
                    id: [
                        { required: true, message: '请选择修改的内容', trigger: 'blur' }
                    ],
					phoneNum: [
						{ required: true, message: '请选择发言类型', trigger: 'blur' }
					],
					userName: [
						{ required: true, message: '请输入发言内容', trigger: 'blur' }
					]
				},
				//编辑界面数据
				editForm: {
					id: null,
					userName: null,
					phoneNum: null
				},

				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
					phoneNum: [
						{ required: true, message: '请选择发言类型', trigger: 'blur' }
					],
					userName: [
						{ required: true, message: '请输入发言内容', trigger: 'blur' }
					]
				},
				//新增界面数据
				addForm: {
					userName: null,
					phoneNum: null
				},
				rooms:[{id:null,roomName:"请选择房间"}],
				accts:[{id:null,acctName:"请选择账户"}],
				parentCatgs:[{id:null,catgName:"请选择父类"}],
				childrenCatgs:[{id:null,catgName:"请选择子类"}],
				pres:[],

			}
		},
		methods: {
			//使用显示转换
            formatTime: function (row, column) {
				return row.createTime  ?  moment(row.createTime).format("YYYY-MM-DD HH:mm:ss") : "";
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getUsers();
			},
			//获取内容列表
			getUsers:function() {
				let params = {
					pageNum: this.page - 1,
                    pageSize:10,
					userName: this.filters.userName,
					phoneNum: this.filters.phoneNum,
				};
				this.listLoading = true;
				NProgress.start();
                listUser(params , this).then((res) => {
                    if(!res){
                        return;
					}
					this.total = res.total;
					this.contents = res.data.data;
					this.listLoading = false;
					NProgress.done();
				});
			},
			//删除
			handleDel: function (index, row) {
                var _this = this;
                util.Msg.warning(_this , null , function(resp) {
                    _this.listLoading = true;
                    NProgress.start();
                    let para = { ids: row.id };
                    delUser(para).then(function(){
                        _this.listLoading = false;
                        NProgress.done();
                        util.Msg.success(_this , null);
                        _this.getUsers();
                    });
                });

			},
			//显示编辑界面
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//显示新增界面
			handleAdd: function () {
				this.addFormVisible = true;
				this.addForm = {
                    userName: null,
                    phoneNum: ''
				};
			},
			//编辑
			editSubmit: function () {
                var _this = this;
				this.$refs.editForm.validate((valid) => {
					if (valid) {
                        util.Msg.confirm(_this , null , function(resp) {
                            _this.editLoading = true;
                            NProgress.start();
                            let para = Object.assign({}, _this.editForm);

                            editUser(para).then(function()  {
                                _this.editLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['editForm'].resetFields();
                                _this.editFormVisible = false;
                                _this.getUsers();
                            });
                        });
					}
				});
			},
			//新增
			addSubmit: function () {
                var _this = this;
				this.$refs.addForm.validate((valid) => {
					if (valid) {
						util.Msg.confirm(_this , null ,function () {
                            _this.addLoading = true;
                            NProgress.start();
                            let para = Object.assign({}, _this.addForm);
                            addUser(para).then(function() {
                                _this.addLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['addForm'].resetFields();
                                _this.addFormVisible = false;
                                _this.getUsers();
                            });
                        });

					}
				});
			},
			selsChange: function (sels) {
				this.sels = sels;
			},
			//批量删除
			batchRemove: function () {
                var _this = this;
                var ids = this.sels.map(item => item.id).toString();
                util.Msg.warning(_this , null ,function (){
                    _this.listLoading = true;
					NProgress.start();
					let para = { ids: ids };
					delUser(para).then(function() {
                        _this.listLoading = false;
						NProgress.done();
                        util.Msg.success(_this);
                        _this.getUsers();
					});
                });
			}
		},
		mounted() {
			this.getUsers();
		}
	}

</script>

<style scoped>

</style>