// package com.function.user;

// import com.microsoft.azure.functions.*;

// import java.util.*;
// import java.util.logging.Logger;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// /**
//  * Unit test for Function class.
//  */
// public class FunctionTest {
//     /**
//      * Unit test for HttpTriggerJava method.
//      */
//     @Test
//     public void testHttpTriggerJava() throws Exception {
//         // Setup
//         @SuppressWarnings("unchecked")
//         final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

//         final Map<String, String> queryParams = new HashMap<>();
//         queryParams.put("name", "Azure");
//         doReturn(queryParams).when(req).getQueryParameters();

//         final Optional<String> queryBody = Optional.empty();
//         doReturn(queryBody).when(req).getBody();

//         doAnswer(invocation -> {
//             HttpStatus status = (HttpStatus) invocation.getArguments()[0];
//             return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
//         }).when(req).createResponseBuilder(any(HttpStatus.class));

//         final ExecutionContext context = mock(ExecutionContext.class);
//         doReturn(Logger.getGlobal()).when(context).getLogger();

//         // Invoke
//         final HttpResponseMessage ret = new Function().HttpTriggerJava(req, context);
      

//         // Verify
//         assertEquals(ret.getStatus(), HttpStatus.OK);
//     }
// }
