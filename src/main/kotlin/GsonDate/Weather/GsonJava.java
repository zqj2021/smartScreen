package GsonDate.Weather;
import java.util.List;
class GsonSuggestWorlds{

    private List<MessageBean> message;
    private Integer status;

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static class MessageBean {
        private String key;
        private String paraphrase;
        private Integer value;
        private List<MeansBean> means;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getParaphrase() {
            return paraphrase;
        }

        public void setParaphrase(String paraphrase) {
            this.paraphrase = paraphrase;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public List<MeansBean> getMeans() {
            return means;
        }

        public void setMeans(List<MeansBean> means) {
            this.means = means;
        }

        public static class MeansBean {
            private String part;
            private List<String> means;

            public String getPart() {
                return part;
            }

            public void setPart(String part) {
                this.part = part;
            }

            public List<String> getMeans() {
                return means;
            }

            public void setMeans(List<String> means) {
                this.means = means;
            }
        }
    }
}