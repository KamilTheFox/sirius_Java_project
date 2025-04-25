package ru.hpclab.hl.module1.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllAverageCheckDTO
{
    public List<AverageCheckDTO> allCheck;

    public AllAverageCheckDTO()
    {
        allCheck = new ArrayList<>();
    }
}
