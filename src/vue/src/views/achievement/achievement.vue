<template>
	<el-form ref="form" :model="form"  label-width="80px" :rules="formRules"   style="margin:20px;width:60%;min-width:600px;">
		<el-form-item label="Cookie" prop="cookie">
			<el-input v-model="form.cookie"></el-input>
		</el-form-item>

		<el-form-item label="开始日期">
			<el-col :span="11">
				<el-date-picker type="date" placeholder="选择日期" v-model="form.fromDate" style="width: 100%;"></el-date-picker>
			</el-col>
		</el-form-item>

		<el-form-item label="结束日期">
			<el-col :span="11">
				<el-date-picker type="date" placeholder="选择日期" v-model="form.toDate" style="width: 100%;"></el-date-picker>
			</el-col>
		</el-form-item>
		<el-form-item label="车牌数据">
			<el-upload ref="fileRef"
					   name="file"
					   action="/achievement/query"
					   :on-preview="handlePreview"
					   :on-error="uploadError"
					   :on-success="uploadSuccess"
					   :before-upload="beforUpload"
					   :limit="1"
					   accept=".xlsx,.xls"
					   :show-file-list="true"
					   :file-list="fileList"
					   :auto-upload="false"
					   :data="form">
				<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
				<el-button style="margin-left: 10px;" size="small" type="success" @click="query">上传到服务器</el-button>
				<div slot="tip" class="el-upload__tip">只能上传Excel文件，且不超过10M</div>
			</el-upload>
		</el-form-item>
	</el-form>

</template>

<script>
	import util from '../../common/js/util';
	import NProgress from 'nprogress';
	import uuid from 'uuid/v1';
	import { downloadAchieve, queryAchieve} from '../../api/api';
	export default {
		data() {
			return {
				form: {
					cookie: '',
					fromDate: '',
					toDate: '',
					desc: '',
					uid:''
				},
				formRules: {
					cookie: [
						{ required: true, message: '请输入cookie（如:JSESSIONID=PNbBl6irrTSRWEsDSQV2xsyMccJoQroc-Lr8HL2f8jtx2LOGSIT3!-771736621）', trigger: 'blur' }
					]
				},
				fileList:[{}]
			}
		},
		methods: {
			beforUpload:function(){
				NProgress.start();

			},
			uploadSuccess:function(){
				let _this = this;
				util.Msg.success(_this , "上传成功");
				NProgress.start();
				downloadAchieve({"uid": _this.form.uid})
			},
			uploadError:function(){
				let _this = this;
				util.Msg.error(_this , "上传失败");
				NProgress.done();
			},
			handlePreview:function(){
				console.info(this.fileList)
			},
			getUId:function(){
				let S4 = function() {
					return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
				};
				return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
			},
			query:function() {
				let _this = this;
				console.info(_this.$refs.fileRef.data);
				_this.$refs.form.validate((valid) => {
					if (valid) {
						util.Msg.confirm(_this , null ,function () {
							NProgress.start();
							console.info(_this);
							_this.form.uid = _this.getUId();
							_this.$refs.fileRef.submit();
							NProgress.done();
					/*		addAcct(para).then(function()  {
								_this.addLoading = false;
								NProgress.done();
								util.Msg.success(_this);
								_this.$refs['form'].resetFields();
								_this.addFormVisible = false;
								_this.getContents();
							});*/
						});
					}
				});
			}
		}
	}

</script>