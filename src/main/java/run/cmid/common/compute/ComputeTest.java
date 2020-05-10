package run.cmid.common.compute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.compute.annotations.ComputeMethod;
import run.cmid.common.compute.annotations.ComputeMethodFieldName;
import run.cmid.common.compute.annotations.EnableCompute;
import run.cmid.common.compute.core.ComputeObject;

/**
 *
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
// getComputs
// getComputs
    }

    private Integer v1 = 10;
    private Integer v2 = 20;

    @ComputeMethod("v1*v2")
    private Double v3;

    @ComputeMethod("computs.v11*computs.v12")
    private Double v4;

    @ComputeMethodFieldName("compute")
    private Double v5;

    @ComputeMethodFieldName("compute")
    private Double v6;

    private String compute = "computs.v11*computs.v12";

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