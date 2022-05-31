<template>
  <j-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
      
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classNo" label="培训班编号">
          <a-input placeholder="请输入培训班编号" v-model="model.classNo" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="holdDate" label="举行日期">
          <a-date-picker v-model="model.holdDate"/>
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="realDate" label="实际举行日期">
          <a-date-picker v-model="model.realDate"/>
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="referNum" label="参与人数">
          <a-input-number v-model="model.referNum"/>
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="realNum" label="实际参与人数">
          <a-input-number v-model="model.realNum"/>
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status" label="典礼状态 0未确认 1制定方案中 2准备开始中 3已完成">
          <a-input placeholder="请输入典礼状态 0未确认 1制定方案中 2准备开始中 3已完成" v-model="model.status" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark" label="备注">
          <a-input placeholder="请输入备注" v-model="model.remark" />
        </a-form-model-item>
		
      </a-form-model>
    </a-spin>
  </j-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import moment from "moment"

  export default {
    name: "GraduationCeremonyModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        validatorRules:{
        },
        url: {
          add: "/graduation/graduationCeremony/add",
          edit: "/graduation/graduationCeremony/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        //初始化默认值
        this.edit({});
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.$refs.form.clearValidate();
      },
      handleOk () {
        const that = this;
        // 触发表单验证
         this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }else{
             return false;
          }
        })
      },
      handleCancel () {
        this.close()
      },


    }
  }
</script>

<style lang="less" scoped>

</style>