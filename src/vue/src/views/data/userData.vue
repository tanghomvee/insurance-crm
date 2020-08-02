<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.frameNo" placeholder="车架号"></el-input>
				</el-form-item>

				<el-form-item>
					<el-button type="primary" v-on:click="getContents">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleMatch">匹配数据</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleDownload">下载匹配数据</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="contents" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<!--<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>-->
			<el-table-column label="自带车牌"  with="50"  		prop="carNo"></el-table-column>
			<el-table-column label="是否匹配"  with="auto"  		prop="matchState" :formatter="formatMatchState"></el-table-column>
			<el-table-column label="车辆品牌"  with="auto"  		prop="brand"></el-table-column>
			<el-table-column label="识别代码"  with="200"  		prop="frameNo"></el-table-column>
			<!--<el-table-column label="发动机号"  with="auto"  		prop="engineNo"></el-table-column>
			<el-table-column label="身份证号"  with="auto"  		prop="idNo"></el-table-column>
			<el-table-column label="所有人"  with="auto"  		prop="owerName"></el-table-column>
			<el-table-column label="发牌日期"  with="auto"  		prop="licenseDate"></el-table-column>
			<el-table-column label="住所"  with="auto"  			 prop="addr"></el-table-column>
			<el-table-column label="手机号码"  with="auto"  		prop="phoneNum"></el-table-column>-->
			<el-table-column label="保险公司"  with="auto"  		prop="company"></el-table-column>
			<el-table-column label="商业险保单号"  with="auto"  	prop="viPolicyNo"></el-table-column>
			<el-table-column label="终保日期"  with="auto"  	    prop="finalInsDate"></el-table-column>
			<el-table-column label="车辆使用价值"  with="auto"  	prop="useVal"></el-table-column>
			<el-table-column label="商业险原价"  with="auto"  	prop="viAmount"></el-table-column>
			<el-table-column label="商业险折扣"  with="auto"  	prop="viDiscount"></el-table-column>
			<el-table-column label="商业险优惠后"  with="auto"  	prop="payViAmt"></el-table-column>
			<el-table-column label="交强险"  with="auto"  		prop="ciAmount"></el-table-column>
			<el-table-column label="车船税"  with="auto"  		prop="vvt"></el-table-column>
			<el-table-column label="内容"  with="auto"  			 prop="content"></el-table-column>

			<el-table-column label="操作" width="150">
				<template scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					<!--<el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>-->
				</template>
			</el-table-column>
		</el-table>

		<!--工具条-->
		<!--<el-col :span="24" class="toolbar">
			<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
			<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
			</el-pagination>
		</el-col>-->

		<!--编辑界面-->
		<el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
				<el-form-item label="自带车牌" prop="carNo">
					<el-input placeHolder="自带车牌" v-model="editForm.carNo">
					</el-input>
				</el-form-item>
				<el-form-item label="车辆品牌" prop="brand">
					<el-input placeHolder="车辆品牌" v-model="editForm.brand">
					</el-input>
				</el-form-item>
				<el-form-item label="识别代码" prop="frameNo">
					<el-input placeHolder="识别代码" v-model="editForm.frameNo">
					</el-input>
				</el-form-item>
				<el-form-item label="发动机号" prop="engineNo">
					<el-input placeHolder="发动机号" v-model="editForm.engineNo">
					</el-input>
				</el-form-item>
				<el-form-item label="身份证号" prop="idNo">
					<el-input placeHolder="身份证号" v-model="editForm.idNo">
					</el-input>
				</el-form-item>
				<el-form-item label="所有人" prop="owerName">
					<el-input placeHolder="所有人" v-model="editForm.owerName">
					</el-input>
				</el-form-item>
				<el-form-item label="发牌日期" prop="licenseDate">
					<el-input placeHolder="发牌日期" v-model="editForm.licenseDate">
					</el-input>
				</el-form-item>
				<el-form-item label="住所" prop="addr">
					<el-input placeHolder="住所" v-model="editForm.addr">
					</el-input>
				</el-form-item>
				<el-form-item label="手机号码" prop="phoneNum">
					<el-input placeHolder="手机号码" v-model="editForm.phoneNum">
					</el-input>
				</el-form-item>
				<el-form-item label="保险公司" prop="company">
					<el-input placeHolder="保险公司" v-model="editForm.company">
					</el-input>
				</el-form-item>
				<el-form-item label="商业险保单号" prop="	viPolicy">
					<el-input placeHolder="商业险保单号" v-model="editForm.viPolicy">
					</el-input>
				</el-form-item>
				<el-form-item label="终保日期" prop="finalInsDate">
					<el-input placeHolder="终保日期" v-model="editForm.finalInsDate">
					</el-input>
				</el-form-item>
				<el-form-item label="车辆使用价值" prop="	useVal">
					<el-input placeHolder="车辆使用价值" v-model="editForm.useVal">
					</el-input>
				</el-form-item>
				<el-form-item label="商业险原价" prop="viAmount">
					<el-input placeHolder="商业险原价" v-model="editForm.viAmount">
					</el-input>
				</el-form-item>
				<el-form-item label="商业险折扣" prop="viDiscount">
					<el-input placeHolder="商业险折扣" v-model="editForm.viDiscount">
					</el-input>
				</el-form-item>
				<el-form-item label="商业险优惠后" prop="	payViAmt">
					<el-input placeHolder="商业险优惠后" v-model="editForm.payViAmt">
					</el-input>
				</el-form-item>
				<el-form-item label="交强险" prop="ciAmount">
					<el-input placeHolder="交强险" v-model="editForm.ciAmount">
					</el-input>
				</el-form-item>
				<el-form-item label="车船税" prop="vvt">
					<el-input placeHolder="车船税" v-model="editForm.vvt">
					</el-input>
				</el-form-item>
				<el-form-item label="内容" prop="content">
					<el-input placeHolder="内容" v-model="editForm.content">
					</el-input>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<!--<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>-->
			</div>
		</el-dialog>
		<!--新增界面-->
		<el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
			<el-upload
					class="filter-item"
					name="file"
					action="/userData/in"
					:on-error="uploadError"
					:on-success="uploadSuccess"
					:before-upload="beforUpload"
					:limit="1"
					accept=".xlsx,.xls"
					:show-file-list="false"
					:file-list="fileList">
				<el-button  style="margin-left: 10px;" icon="el-icon-edit" type="primary">批量上传</el-button>
				<div slot="tip" class="el-upload__tip">只能上传Excel文件，且不超过10M</div>
			</el-upload>
		</el-dialog>
		<!--下载界面-->
		<el-dialog title="下载匹配数据" v-model="downloadFormVisible" :close-on-click-modal="false">
			<el-form :model="downloadForm" label-width="80px"  ref="downloadForm">

				<el-form-item label="时间端">
					<el-date-picker type="date" v-model="downloadForm.beginDate"  placeholder="开始日期"></el-date-picker>
					<el-date-picker type="date" v-model="downloadForm.finishDate" placeholder="结束日期"></el-date-picker>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="downloadFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="downloadData()" :loading="downloading">下载</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
    import util from '../../common/js/util';
    import NProgress from 'nprogress';
	import { download, listUserData} from '../../api/api';

    export default {
		methods: {
			formatMatchState: function (row, column) {
				return row.matchState == "MATCHED"  ? "匹配" : "未匹配";
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getContents();
			},
			//获取内容列表
			getContents:function() {
				let params = {
					pageNum: this.page - 1,
					pageSize:10,
					cardNo: this.filters.carNo || null,
					frameNo: this.filters.frameNo || null
				};
				this.listLoading = true;
				NProgress.start();
				listUserData(params , this).then((res) => {
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
				util.Msg.warning(_this , null ,function () {
					_this.listLoading = true;
					NProgress.start();
					let para = { ids: row.id };
					delData(para).then(function() {
						_this.listLoading = false;
						NProgress.done();
						util.Msg.success(_this);
						_this.getContents();
					});
				});
			},
			//显示编辑界面
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//显示新增界面
			handleMatch: function () {
				this.addFormVisible = true;
			},
			handleDownload: function () {
				this.downloadFormVisible = true;
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
								_this.getContents();
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
								_this.getContents();
							});
						});
					}
				});
			},
			//下载
			downloadData: function () {
				var _this = this;

				_this.downloading = true;
				download(_this.downloadForm , _this);
				_this.downloadFormVisible = false;
				_this.downloading = false;
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
						_this.getContents();
					});
				});
			},
			beforUpload:function(){
				this.addLoading = true;
				NProgress.start();
			},
			uploadSuccess:function(rsp){
				var _this = this;

				_this.addLoading = false;
				NProgress.done();
				_this.addFormVisible = false;

				if (rsp.code == "error"){
					util.Msg.error(_this , rsp.msg);
					return;
				}
				util.Msg.success(_this , "上传成功");
				_this.getContents();
			},

			uploadError:function(){
				var _this = this;
				util.Msg.error(_this , "上传失败");
				_this.addLoading = false;
				NProgress.done();
				_this.getContents();
				_this.addFormVisible = false;
			},
		},
		data:function() {
			return {
				filters: {
                    frameNo:null
				},
                contents: [],
				total: 0,
				page: 1,
				listLoading: false,
				sels: [],//列表选中列
				fileList:[],
				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
                    id: [
                        { required: true, message: '请选择修改的内容', trigger: 'blur' }
                    ]
				},
				//编辑界面数据
				editForm: {
					id: null,
					/**自带车牌*/
					carNo:"",
					/**车辆品牌*/
					brand:"",
					/**识别代码*/
					frameNo:"",
					/**发动机号*/
					engineNo:"",
					/**身份证号*/
					idNo:"",
					/**所有人*/
					owerName:"",
					/**发牌日期*/
					licenseDate:"",
					/**住所*/
					addr:"",
					/**手机号码*/
					phoneNum:"",
					/**保险公司*/
					company:"",
					/**商业险保单号*/
					viPolicyNo:"",
					/**终保日期*/
					finalInsDate:"",
					/**车辆使用价值*/
					useVal:"",
					/**商业险原价*/
					viAmount:"",
					/**商业险折扣*/
					viDiscount:"",
					/**商业险优惠后*/
					payViAmt:"",
					/**交强险*/
					ciAmount:"",
					/**车船税*/
					vvt:"",
					/**内容*/
					content:""
				},
				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				//下载界面
				downloadForm:{
					beginDate:null,
					finishDate:null
				},
				downloadFormVisible:false,
				downloading:false

			}
		},
		mounted() {
			this.getContents();
		}
	}

</script>

<style scoped>

</style>