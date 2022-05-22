<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <j-file-upload
        ref="jFileUpload"
        :disabled="false"
        businessType="declare_result"
        objectId="declare_result"
        dict="file_declare_result"
        :labelColumn="{ span: 0 }"
        :wrapperColumn="{ span: 24 }"
      />
    </a-card>
  </page-layout>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'

  export default {
    name: 'TenderResult',
    components: {
      PageLayout
    },
    data () {
      return {
        title: '招标结果'
      }
    }
  }
</script>

<style lang="less" scoped>
</style>