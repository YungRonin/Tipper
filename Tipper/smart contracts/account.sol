pragma solidity ^0.4.18;

contract Token {

    /// @return total amount of tokens
    function totalSupply() constant returns (uint256 supply) {}

    /// @param _owner The address from which the balance will be retrieved
    /// @return The balance
    function balanceOf(address _owner) constant returns (uint256 balance) {}

    /// @notice send `_value` token to `_to` from `msg.sender`
    /// @param _to The address of the recipient
    /// @param _value The amount of token to be transferred
    /// @return Whether the transfer was successful or not
    function transfer(address _to, uint256 _value) returns (bool success) {}

    /// @notice send `_value` token to `_to` from `_from` on the condition it is approved by `_from`
    /// @param _from The address of the sender
    /// @param _to The address of the recipient
    /// @param _value The amount of token to be transferred
    /// @return Whether the transfer was successful or not
    function transferFrom(address _from, address _to, uint256 _value) returns (bool success) {}

    /// @notice `msg.sender` approves `_addr` to spend `_value` tokens
    /// @param _spender The address of the account able to transfer the tokens
    /// @param _value The amount of wei to be approved for transfer
    /// @return Whether the approval was successful or not
    function approve(address _spender, uint256 _value) returns (bool success) {}

    /// @param _owner The address of the account owning tokens
    /// @param _spender The address of the account able to transfer the tokens
    /// @return Amount of remaining tokens allowed to spent
    function allowance(address _owner, address _spender) constant returns (uint256 remaining) {}

    event Transfer(address indexed _from, address indexed _to, uint256 _value);
    event Approval(address indexed _owner, address indexed _spender, uint256 _value);
}

contract Escrow {
    address public escrow;
    uint256 public alphaTokenPrice;
    uint256 public betaTokenPrice;
    token public alphaToken;
    token public betaToken;
    token public omegaToken;

    constructor(
        address omegaTokenAddress,
        address alphaTokenAddress,
        address betaTokenAddress
    ) {
        alphaToken = token(alphaTokenAddress);
        betaToken = token(betaTokenAddress);
        omegaToken = token(omegaTokenAddress);
        alphaTokenPrice = uint(0.1);
        betaTokenPrice = uint(0.2);
    }

    function sendOmega(uint256 _value) public {
      require(alphaToken.transferFrom(msg.sender, address(this), uint256(_value / alphaTokenPrice)));
      require(betaToken.transferFrom(msg.sender, address(this), _value / betaTokenPrice));
      omegaToken.transfer(msg.sender, _value);
    }

    function receiveOmega(uint256 _value) public {
      require(omegaToken.transferFrom(msg.sender, address(this), _value));
      alphaToken.transfer(msg.sender, _value / alphaTokenPrice);
      betaToken.transfer(msg.sender, _value / betaTokenPrice);
    }
}
