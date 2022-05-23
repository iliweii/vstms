<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <j-file-upload
        ref="jFileUpload"
        :disabled="false"
        businessType="training_preparatory_meeting"
        objectId="training_preparatory_meeting"
        :labelColumn="{ span: 0 }"
        :wrapperColumn="{ span: 24 }"
        :extra="[
          {
            name: '附件',
            emit: 'attach',
          },
        ]"
        @attach="handleAttach"
      />
    </a-card>

    <preparatory-meeting-attach ref="modalForm"></preparatory-meeting-attach>
  </page-layout>
</template>

<script>
import PageLayout from '@/components/page/PageLayout'
import PreparatoryMeetingAttach from './modules/PreparatoryMeetingAttach'

export default {
  name: 'PreparatoryMeeting',
  components: {
    PageLayout,
    PreparatoryMeetingAttach,
  },
  data() {
    return {
      title: '筹备会记录',
    }
  },
  methods: {
    handleAttach(attach) {
      this.$refs.modalForm.title = '附件'
      this.$refs.modalForm.object = attach
      this.$refs.modalForm.disableSubmit = true
    },
  },
}
</script>

<style lang="less" scoped>
</style>