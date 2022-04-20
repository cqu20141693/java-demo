/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gow.pulsar.manager.tokens.api;


import com.google.common.collect.Maps;
import com.gow.pulsar.core.manager.token.service.JwtService;
import com.gow.pulsar.manager.gen.dao.TokensMapper;
import com.gow.pulsar.manager.gen.model.Tokens;
import com.gow.pulsar.manager.gen.model.TokensExample;
import com.gow.pulsar.manager.tokens.model.BrokerTokenEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Broker tokens controller for broker token create, delete, update and search
 */
@RequestMapping(value = "/pulsar-manager")
@Api("Support more management token.")
@Validated
@RestController
@Slf4j
public class BrokerTokensController {

    private final JwtService jwtService;

    @Autowired
    private TokensMapper brokerTokensMapper;

    @Autowired
    public BrokerTokensController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @ApiOperation(value = "Get the list of existing broker tokens, support paging, the default is 10 per page")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/tokens", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getEnvironmentsList(
            @ApiParam(value = "page_num", defaultValue = "1", example = "1")
            @RequestParam(name = "page_num", defaultValue = "1")
            @Min(value = 1, message = "page_num is incorrect, should be greater than 0.")
                    Integer pageNum,
            @ApiParam(value = "page_size", defaultValue = "10", example = "10")
            @RequestParam(name = "page_size", defaultValue = "10")
            @Range(min = 1, max = 1000, message = "page_size is incorrect, should be greater than 0 and less than "
                    + "1000.")
                    Integer pageSize) {
        //PageHelper.startPage(pageNum, pageSize);
        TokensExample example = new TokensExample();
        long total = brokerTokensMapper.countByExample(example);
        example.page(pageNum, pageSize);
        List<Tokens> tokens = brokerTokensMapper.selectByExample(example);
        Map<String, Object> result = Maps.newHashMap();
        result.put("total", total);
        result.put("data", tokens);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Add token for connect broker")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/tokens/token", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> addBrokerToken(@RequestBody BrokerTokenEntity brokerTokenEntity) {
        TokensExample example = new TokensExample();
        TokensExample.Criteria criteria = example.createCriteria();
        criteria.andRoleEqualTo(brokerTokenEntity.getRole());

        Tokens tokens = brokerTokensMapper.selectOneByExample(example);
        Map<String, Object> result = Maps.newHashMap();
        if (tokens != null) {
            log.info("role={} token={}", brokerTokenEntity.getRole(), tokens.getToken());
        }

        String token = jwtService.createBrokerToken(brokerTokenEntity.getRole(), null);
        if (token == null) {
            result.put("error", "Token generate failed");
            return ResponseEntity.ok(result);
        }
        Tokens tokens1 = new Tokens();
        tokens1.setToken(token);
        tokens1.setRole(brokerTokenEntity.getRole());
        brokerTokenEntity.setToken(token);
        //    long brokerTokenId = brokerTokensMapper.insert(tokens1);
//        if (brokerTokenId <= 0) {
//            result.put("error", "Token save Failed");
//            return ResponseEntity.ok(result);
//        }
        result.put("message", "Token generate and save success");
        //  result.put("tokenId", brokerTokenId);
        result.put("token", token);
        return ResponseEntity.ok(result);
    }


    @ApiOperation(value = "Update token for connect broker")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/tokens/token", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> updateBrokerToken(@RequestBody BrokerTokenEntity brokerTokenEntity) {
//        BrokerTokenEntity tokenByRole = brokerTokensMapper.findTokenByRole(brokerTokenEntity.getRole());
//        Map<String, Object> result = Maps.newHashMap();
//        if (tokenByRole == null) {
//            result.put("error", "Role is not exist");
//            return ResponseEntity.ok(result);
//        }
//        BrokerTokenEntity getBrokerTokenEntity = tokenByRole;
//        brokerTokenEntity.setToken(getBrokerTokenEntity.getToken());
//        brokerTokensRepository.update(brokerTokenEntity);
//        result.put("message", "Token generate and save success");
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "Get token by role")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/tokens/{role}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> getBrokerToken(@PathVariable String role) {
//        BrokerTokenEntity tokenByRole = brokerTokensMapper.findTokenByRole(role);
//        Map<String, Object> result = Maps.newHashMap();
//        if (tokenByRole == null) {
//            result.put("error", "Token not find");
//            return ResponseEntity.ok(result);
//        }
//        BrokerTokenEntity brokerTokenEntity = tokenByRole;
//        result.put("token", brokerTokenEntity.getToken());
//        result.put("description", brokerTokenEntity.getDescription());
//        result.put("role", brokerTokenEntity.getRole());
//        result.put("tokenId", brokerTokenEntity.getTokenId());
        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "Delete token")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/tokens/{role}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteBrokerToken(@PathVariable String role) {
//        BrokerTokenEntity tokenByRole = brokerTokensMapper.findTokenByRole(role);
//        Map<String, Object> result = Maps.newHashMap();
//        if (tokenByRole == null) {
//            result.put("error", "Token not find");
//            return ResponseEntity.ok(result);
//        }
//        brokerTokensMapper.delete(role);
//        result.put("message", "Delete broker token success");
//        result.put("role", role);
        return ResponseEntity.ok(null);
    }
}
