<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <j-file-upload
        ref="jFileUpload"
        :disabled="false"
        businessType="declare_notice"
        objectId="declare_notice"
        dict="file_declare_notice"
        :labelColumn="{ span: 0 }"
        :wrapperColumn="{ span: 24 }"
        :extra="[{
          name: '附件',
          emit: 'attach'
        }]"
        @attach="handleAttach"
      />
    </a-card>

    <project-office-notice-attach ref="modalForm"></project-office-notice-attach>
  </page-layout>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'
  import ProjectOfficeNoticeAttach from './modules/ProjectOfficeNoticeAttach'

  export default {
    name: 'ProjectOfficeNotice',
    components: {
      PageLayout,
      ProjectOfficeNoticeAttach
    },
    data () {
      return {
        title: '项目办通知文件'
      }
    },
    methods: {
      handleAttach (attach) {
        this.$refs.modalForm.title = '附件'
        this.$refs.modalForm.object = attach
        this.$refs.modalForm.disableSubmit = true
      }
    }
  }
</script>

<style lang="less" scoped>
</style>