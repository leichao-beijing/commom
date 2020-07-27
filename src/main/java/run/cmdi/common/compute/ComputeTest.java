package run.cmdi.common.compute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmdi.common.compute.annotations.ComputeMethod;
import run.cmdi.common.compute.annotations.ComputeMethodFieldName;
import run.cmdi.common.compute.annotations.EnableCompute;
import run.cmdi.common.compute.core.ComputeObject;

/**
 * @author leichao
 * @date 2020-04-13 09:16:21
 */
@EnableCompute
@Getter
@Setter
@ToString
public class ComputeTest {
    // Log log = LogFactory.getLog(this.getClass());

    public static void main(String[] args) {
        ComputeTest computeTest = new ComputeTest();
        ComputeObject.compute(computeTest);
        computeTest.getCompute();
    }

    private Integer v1 = 10;
    private Integer v2 = 20;

    @ComputeMethod("v1*v2")
    private Double v3;

    @ComputeMethod("computes.v11*computes.v12")
    private Double v4;

    @ComputeMethodFieldName("compute")
    private Double v5;

    @ComputeMethodFieldName("compute")
    private Double v6;

    private String compute = "computes.v11*computes.v12";

    private Compute computes = new Compute();
}

@Getter
@Setter
@ToString
class Compute {
    private Integer v10 = 10;
    private Integer v11 = 22;
    @ComputeMethod("(v10*2)")
    private Integer v12;
    @ComputeMethod("(v12*v11)-(v12/2)")
    private Double v13;

}