package model;
import util.enumConstant.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;




public class Epic extends Task {
    private LocalDateTime endTime;
    private List<Integer> subTasksEpic = new ArrayList<>();

    public Epic(String name, String description) {
        super(name,description,Status.NEW,Duration.ofMinutes(0),LocalDateTime.now());

    }

    public List<Integer> getSubTasksEpic() {
        return subTasksEpic;
    }

    public void setSubTasksEpic(List<Integer> subTasksEpic) {
        if (subTasksEpic == null) {
            this.subTasksEpic.clear();
        } else {
            this.subTasksEpic = subTasksEpic;
        }
    }

   // @Override
   // public LocalDateTime getEndTime() {
      //  return endTime;
   // }

    public void setEndTime(LocalDateTime time) {
        this.endTime = time;
    }

    @Override
    public Types getType() {
        return Types.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", subTasksEpic=" + subTasksEpic +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Epic epic = (Epic) object;
        return Objects.equals(subTasksEpic, epic.subTasksEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksEpic);
    }
}