<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item label="选择用户">
					<el-select v-model="filters.userId" placeholder="选择用户" :clearable="true">
						<el-option v-for="item in users" :key="item.id" :value="item.id" :label="item.userName"></el-option>
					</el-select>
				</el-form-item>
				
				<el-form-item>
					<el-button type="primary" v-on:click="getAccts">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="accts" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>
			<el-table-column prop="userName" label="用户名" width="auto">
			</el-table-column>
			
			<el-table-column prop="acctName" label="账户名称" width="auto">
			</el-table-column>

			<el-table-column prop="credit" label="积分" width="auto">
			</el-table-column>

			<el-table-column label="操作" width="200">
				<template scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<el-button type="primary" size="small" @click="handleCharge(scope.$index, scope.row)">充值</el-button>
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
				<el-form-item label="选择用户" prop="userId">
					<el-select v-model="editForm.userId" placeholder="选择用户" :clearable="true">
						<el-option v-for="item in users" :key="item.id" :value="item.id" :label="item.userName"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item prop="acctName" label="账户名称">
					<el-input v-model="editForm.acctName" placeholder="账户名称"></el-input>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
			</div>
		</el-dialog>
		<!--充值界面-->
		<el-dialog title="充值" v-model="chargeFormVisible" :close-on-click-modal="false">
			<el-form :model="chargeForm" label-width="80px" :rules="chargeFormRules" ref="chargeForm">
				<el-form-item label="选择用户" prop="userId">
					<el-select v-model="chargeForm.userId" placeholder="选择用户" :disabled="true">
						<el-option v-for="item in users" :key="item.id" :value="item.id" :label="item.userName"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item prop="acctName" label="账户名称">
					<el-input v-model="chargeForm.acctName" placeholder="账户名称" :disabled="true"></el-input>
				</el-form-item>

				<el-form-item prop="credit" label="账户积分">
					<el-input v-model="chargeForm.credit" placeholder="账户积分"></el-input>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="chargeFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="chargeSubmit" :loading="chargeLoading">提交</el-button>
			</div>
		</el-dialog>

		<!--新增界面-->
		<el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
			<el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">

				<el-form-item label="选择用户" prop="userId">
					<el-select v-model="addForm.userId" placeholder="选择用户" :clearable="true">
						<el-option v-for="item in users" :key="item.id" :value="item.id" :label="item.userName"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item prop="acctName" label="账户名称">
					<el-input v-model="addForm.acctName" placeholder="账户名称"></el-input>
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
    import NProgress from 'nprogress';
	import {addAcct, delAcct,chargeAcct, editAcct, listAcct, listUser} from '../../api/api';

    export default {
		data:function() {
			return {
				filters: {
                    userId:null
				},
                accts: [],
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
					acctName: [
						{ required: true, message: '请输入账户名', trigger: 'blur' }
					],
					userId: [
						{ required: true, message: '请选择用户', trigger: 'blur',type:"number"  }
					]
				},
				//编辑界面数据
				editForm: {
					id: null,
					userId:null,
                    userName: '',
                    acctName: ''
				},
				chargeFormVisible: false,//编辑充值是否显示
				chargeLoading: false,
				chargeFormRules: {
					credit: [
						//{ required: true, message: '账户积分错误', trigger: 'blur',type:"number"  }
					]
				},
				//编辑界面数据
				chargeForm: {
					id: null,
					userId:null,
                    userName: '',
                    acctName: '',
					credit:"",
				},

				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
                    acctName: [
                        { required: true, message: '请输入账户名', trigger: 'blur' }
                    ],
                    userId: [
                        { required: true ,message: '请选择用户', trigger: 'blur',type: "number" }
                    ]
				},
				//新增界面数据
				addForm: {
					userId:null,
					userName: '',
					acctName: ''
				},
				users:[{id:null,userName:"选择用户"}],

			}
		},
		methods: {
			
			handleCurrentChange:function(val) {
				this.page = val;
				this.getAccts();
			},
			//获取内容列表
			getAccts:function() {
				let params = {
					pageNum: this.page - 1,
                    pageSize:10,
					userId: this.filters.userId
				};
				this.listLoading = true;
				NProgress.start();
                listAcct(params , this).then((res) => {
                    if(!res){
                        return;
					}
					this.total = res.total;
					this.accts = res.data.data;
					this.listLoading = false;
					NProgress.done();
				});
			},
			//获取用户列表
			getUsers:function() {
				let params = {
					pageNum: 0,
					pageSize:100000
				};
				this.listLoading = true;
				NProgress.start();
				listUser(params , this).then((res) => {
					if(!res){
						return;
					}
					this.users = res.data.data;
					this.listLoading = false;
					NProgress.done();
				});
			},
			//删除
			handleDel: function (index, row) {
                var _this = this;
                util.Msg.warning(_this , null ,function () {
                    _this.listLoading = true;
                    NProgress.start();
                    let para = { ids: row.id };
                    delAcct(para).then(function() {
                        _this.listLoading = false;
                        NProgress.done();
                        util.Msg.success(_this);
                        _this.getAccts();
                    });
                });
			},
			//显示编辑界面
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//显示充值界面
			handleCharge: function (index, row) {
				this.chargeFormVisible = true;
				this.chargeForm = Object.assign({}, row);
			},
			//显示新增界面
			handleAdd: function () {
				this.addFormVisible = true;
			},
			//编辑
			editSubmit: function () {
                var _this = this;
				this.$refs.editForm.validate((valid) => {
					if (valid) {
                        util.Msg.confirm(_this , null ,function () {
                            _this.editLoading = true;
                            NProgress.start();
                            let para = Object.assign({}, _this.editForm);
                            editAcct(para).then(function(){
                                _this.editLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['editForm'].resetFields();
                                _this.editFormVisible = false;
                                _this.getAccts();
                            });
                        });
					}
				});
			},
			//充值
			chargeSubmit: function () {
                var _this = this;
				this.$refs.chargeForm.validate((valid) => {
					if (valid) {
                        util.Msg.confirm(_this , null ,function () {
                            _this.chargeLoading = true;
                            NProgress.start();
                            let para = Object.assign({}, _this.chargeForm);
                            chargeAcct({"id" : para.id,"credit" : para.credit}).then(function(){
                                _this.chargeLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['chargeForm'].resetFields();
                                _this.chargeFormVisible = false;
                                _this.getAccts();
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
                            addAcct(para).then(function()  {
                                _this.addLoading = false;
                                NProgress.done();
                                util.Msg.success(_this);
                                _this.$refs['addForm'].resetFields();
                                _this.addFormVisible = false;
                                _this.getAccts();
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
                util.Msg.warning(_this , null ,function () {
                    _this.listLoading = true;
                    NProgress.start();
                    let para = { ids: ids };
                    delAcct(para).then(function() {
                        _this.listLoading = false;
                        NProgress.done();
                        util.Msg.success(_this);
                        _this.getAccts();
                    });
                });
			}
		},
		mounted() {
			this.getAccts();
			this.getUsers();
		}
	}

</script>

<style scoped>

</style>