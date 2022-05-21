<template>
  <div>
    <a-drawer
      :title="title"
      :maskClosable="true"
      :width="drawerWidth"
      :destroyOnClose="true"
      placement="right"
      :closable="true"
      @close="handleCancel"
      :visible="disableSubmit"
      style="height: 100%; overflow: auto; padding-bottom: 53px"
    >
      <template slot="title">
        <div style="width: 100%; height: 30px">
          <span>{{ title }}</span>
          <span style="display: inline-block; width: calc(100% - 51px); padding-right: 10px; text-align: right">
            <a-button @click="toggleScreen" icon="appstore" style="height: 20px; width: 20px; border: 0px"></a-button>
          </span>
        </div>
      </template>

      <a-card :bordered="false">
        <j-file-upload
          ref="jFileUpload"
          :disabled="false"
          text="上传附件"
          businessType="declare_notice_attach"
          :objectId="object.id"
          :labelColumn="{ span: 0 }"
          :wrapperColumn="{ span: 24 }"
        />
      </a-card>

    </a-drawer>
  </div>
</template>

<script>

export default {
  name: 'ProjectOfficeNoticeAttach',
  props: {
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  components: {
  },
  data() {
    return {
      drawerWidth: '50%',
      title: '操作',
      visible: false,
      disableSubmit: false,
      activeKey: '1',
      object: {}
    }
  },
  methods: {
    // 窗口最大化切换
    toggleScreen() {
      if (this.modaltoggleFlag) {
        this.modalWidth = window.innerWidth
      } else {
        this.modalWidth = 800
      }
      this.modaltoggleFlag = !this.modaltoggleFlag
    },
    handleCancel() {
      this.object = {}
      this.disableSubmit = false
      this.$emit('ok')
    },
  },
}
</script>

<style scoped lang="less">
</style>
