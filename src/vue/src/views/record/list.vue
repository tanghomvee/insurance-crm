<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item label="选择消费者">
					<el-select v-model="filters.operatorId" placeholder="选择消费者" :clearable="true">
						<el-option v-for="item in users" :key="item.id" :value="item.id" :label="item.userName"></el-option>
					</el-select>
				</el-form-item>
				
				<el-form-item>
					<el-button type="primary" v-on:click="getRecords">查询</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="records" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<!--<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>-->
			<el-table-column prop="userName" label="用户名" width="auto">
			</el-table-column>
			
			<el-table-column prop="acctName" label="账户名称" width="auto">
			</el-table-column>

			<el-table-column prop="tradeCredit" label="交易积分" width="auto">
			</el-table-column>
			<el-table-column prop="tradeDate" label="交易时间" :formatter="formatTime" width="auto">
			</el-table-column>
			<el-table-column prop="tradeType" label="交易类型" width="auto">
			</el-table-column>
			<el-table-column prop="operatorName" label="消费者" width="auto">
			</el-table-column>
			<el-table-column prop="note" label="备注信息" width="auto">
			</el-table-column>

			<el-table-column label="操作" width="200">
				<template scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">详情</el-button>
				</template>
			</el-table-column>
		</el-table>


		<!--详情界面-->
		<el-dialog title="详情" v-model="editFormVisible" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="80px" ref="editForm">

				<el-form-item prop="userName" label="用户名称">
					<el-input v-model="editForm.userName" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item prop="acctName" label="账户名称">
					<el-input v-model="editForm.acctName" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item prop="operatorName" label="消费者名称">
					<el-input v-model="editForm.acctName" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item prop="tradeCredit" label="交易积分">
					<el-input v-model="editForm.tradeCredit" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item prop="tradeDate" label="交易时间">
					<el-input v-model="editForm.tradeDate" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item prop="tradeType" label="交易类型">
					<el-input v-model="editForm.tradeType" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item prop="note" label="备注信息">
					<el-input v-model="editForm.note" :disabled="true"></el-input>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
    import util from '../../common/js/util';
    import NProgress from 'nprogress';
	import {listRecord, listUser} from '../../api/api';
	import moment from "moment";

    export default {
		data:function() {
			return {
				filters: {
					operatorId:null
				},
                records: [],
				total: 0,
				page: 1,
				listLoading: false,
				sels: [],//列表选中列

				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				//编辑界面数据
				editForm: {
					id: null,
					userName:"",
					acctName:"",
					tradeCredit:"",
					tradeDate:"",
					tradeType:"",
					operatorName:"",
					note:""
				},
				users:[{id:null,userName:"选择消费者"}],

			}
		},
		methods: {
			formatTime: function (row, column) {
				return row.tradeDate  ?  moment(row.tradeDate).format("YYYY-MM-DD HH:mm:ss") : "";
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getRecords();
			},
			//获取内容列表
			getRecords:function() {
				let params = {
					pageNum: this.page - 1,
                    pageSize:10,
					operatorId: this.filters.operatorId
				};
				this.listLoading = true;
				NProgress.start();
				listRecord(params , this).then((res) => {
                    if(!res){
                        return;
					}
					this.total = res.total;
					this.records = res.data.data;
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
			//显示编辑界面
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			selsChange: function (sels) {
				this.sels = sels;
			}

		},
		mounted() {
			this.getRecords();
			this.getUsers();
		}
	}

</script>

<style scoped>

</style>