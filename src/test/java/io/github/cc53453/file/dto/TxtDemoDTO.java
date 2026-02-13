package io.github.cc53453.file.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TxtDemoDTO implements TxtLineDTO {
    private Integer id;
    private String name;
    @Override
    public void getDataFromList(List<String> line) {
        id = Integer.valueOf(line.get(0).trim());
        name = line.get(1).trim();
    }
    @Override
    public String toTxtLine() {
        return String.format("%d,%s", id, name);
    }
}