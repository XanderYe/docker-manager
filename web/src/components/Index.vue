<template>
  <div>
    <div>
      <Button :disabled="selectDisabled" style="width: 120px">编辑</Button>
      <Select :disabled="selectDisabled" placeholder="操作" style="width: 120px">
        <Option v-for="option in optionList" :key="option.id" :value="option.id" :disabled="option.disabled">{{option.name}}</Option>
      </Select>
    </div>
    <Table :columns="columns" :data="containerList">
    </Table>
  </div>
</template>

<script>
  export default {
    name: "Index",
    data() {
      return {
        switchLoading: false,
        selectDisabled: true,
        optionList: [
          {
            id: 1,
            name: "启动",
            disabled: true
          },
          {
            id: 2,
            name: "停止",
            disabled: true
          },
          {
            id: 3,
            name: "重新启动",
            disabled: false
          }
        ],
        containerList: [],
        columns: [
          {
            title: "名称",
            key: "name",
            render: (h, params) => {
              const row = params.row;
              const name = row.name;
              const image = row.image;
              return h("div", [
                h('div', name),
                h('div', image)
              ]);
            }
          },
          {
            title: "状态",
            key: "state",
            render: (h, params) => {
              const row = params.row;
              const state = row.state;
              const startTime = row.startTime;
              let text;
              if (state === "运行中" && startTime) {
                let time = new Date(startTime);
                let now = new Date();
                let last = now.getTime() - time.getTime();
                text = this.parseTime(last);
              }
              return h("div", [
                h('div', state),
                h('div', text)
              ]);
            }
          },
          {
            title: "操作",
            key: "operate",
            render: (h, params) => {
              return h('div', [
                h('i-switch', {
                  props: {
                    value: params.row.state === "运行中"
                  },
                  on: {
                    'on-change': (value) => {
                      this.switchContainer(params.row, value)
                    }
                  }
                })
              ])
            }
          }
        ]
      }
    },
    methods: {
      getContainerStatusList() {
        this.$requests.get("/container/getContainerStatusList", {}).then(res => {
          if (res.data.code === 0) {
            this.containerList = res.data.data;
          }
        });
      },
      switchContainer(container, value) {
        this.switchLoading = true;
        if (value) {
          this.startContainer(container.id);
        } else {
          this.stopContainer(container.id);
        }
      },
      startContainer(id) {
        this.$requests.post("/container/start", {id: id}).then(res => {
          if (res.data.code === 0) {
            this.$Message.success("启动成功");
          } else {
            this.$Message.error(res.data.msg);
          }
          this.switchLoading = false;
        })
      },
      stopContainer(id) {
        this.$requests.post("/container/stop", {id: id}).then(res => {
          if (res.data.code === 0) {
            this.$Message.success("停止成功");
          } else {
            this.$Message.error(res.data.msg);
          }
          this.switchLoading = false;
        })
      },
      restartContainer(id) {
        this.$requests.post("/container/restart", {id: id}).then(res => {
          if (res.data.code === 0) {
            this.$Message.success("重启成功");
          } else {
            this.$Message.error(res.data.msg);
          }
          this.switchLoading = false;
        })
      },
      parseTime(last) {
        let text = "Up for ";
        const mins = 60 * 1000;
        const hours = 60 * mins;
        const days = 24 * hours;
        if (last < mins) {
          text += "1 minutes";
        } else if (last < hours) {
          text += parseInt(last / mins) + " minutes";
        } else if (last < days) {
          text += parseInt(last / hours) + " hours";
        } else {
          text += parseInt(last / days) + " days";
        }
        return text;
      }
    },
    created() {
      this.getContainerStatusList();
    }
  }
</script>

<style scoped>

</style>
