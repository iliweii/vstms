<template>
  <a-card :bordered="false">
    <a-spin :spinning="false">
      <!-- 附件 -->
      <j-file-upload
        ref="jFileUpload"
        :disabled="disabled"
        businessType="graduation_select_class_plan"
        :objectId="object.classNo"
        label="方案"
        text="点击添加评选优秀学员方案"
        :labelColumn="{ span: 1 }"
        :wrapperColumn="{ span: 22 }"
      />
    </a-spin>

    <!-- 完成按钮 -->
    <div :style="{ marginTop: '15px' }">
      <a-button @click="() => handleSubmit(model)" type="primary" :disabled="disabled" icon="check"
        >完成评选优秀学员方案上传</a-button
      >
      <div class="ant-form-extra" :style="{ fontSize: '12px' }">
        完成后方可执行后续步骤（评选优秀学员筛选），完成后不可撤销
      </div>
    </div>
  </a-card>
</template>

<script>
import { putAction } from '@/api/manage'

export default {
  name: 'SelectClassPlan',
  components: {},
  props: {
    // 1. 对象数据
    object: Object,
    // 通用禁用标识
    disabled: {
      type: Boolean,
      default: true,
      required: false,
    },
  },
  data() {
    return {
      model: this.object,
      validatorRules: {},
      url: {
        edit: '/graduation/graduationSelectClass/edit',
      },
    }
  },
  methods: {
    handleSubmit(param) {
      const that = this
      param.status = '2'
      putAction(that.url.edit, param).then((res) => {
        that.$message.success('提交成功')
        that.object.status = '2'
        that.$emit('change', that.object)
      })
    },
  },
}
</script>

<style lang="less" scoped>
</style>